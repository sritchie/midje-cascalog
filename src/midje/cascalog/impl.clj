(ns midje.cascalog.impl
  (:use midje.sweet
        [clojure.set :only (difference)]
        [cascalog.api :only (with-job-conf <- ??-)])
  (:require [cascalog.io :as io]))

(defn multifn? [x]
  (instance? clojure.lang.MultiFn x))

(def mocking-forms
  #{'against-background 'provided})

(defn mocking-form?
  "Returns true if the supplied form (or sequence) is a midje
   `provided` or `against-background` clause, false otherwise."
  [x]
  (when (coll? x)
    (contains? mocking-forms (first x))))

(defn extract-mockers
  "Returns a vector of two sequences, obtained by splitting the
  supplied `coll` into midje forms and rest."
  [coll]
  ((juxt filter remove) mocking-form? coll))

(defn pop-log-level
  "Accepts a sequence with an optional log level as its first argument
  and returns a 2-vector with the log level (or nil if it wasn't
  present) and the non-log-level elements of the sequence."
  [bindings]
  (if-let [ll (first (filter io/log-levels bindings))]
    [ll (disj (set bindings) ll)]
    [nil bindings]))

(defn execute
  "Executes the supplied query and returns the sequence of tuples it
  generates. Optionally accepts a log-level key."
  [query & {:keys [log-level]}]
  (let [ll (or log-level :fatal)]
    (io/with-log-level ll
      (with-job-conf {"io.sort.mb" 10}
        (first (??- query))))))

;; ## Midje-Style Checker Helpers

(def log-level-set
  (set (keys io/log-levels)))

(defn mk-opt-set
  "Accepts a sequence of options and returns the same sequence with
  all log-level keywords removed."
  [opts]
  (-> (set opts)
      (difference log-level-set)))

(defn valid-options?
  "Returns false if supplied-opts contains any item not present in
  `permitted-opts` or `log-level-set`, true otherwise."
  [permitted-opts supplied-opts]
  (empty? (difference (set supplied-opts)
                      log-level-set
                      (set permitted-opts))))

(def ^{:doc "Accepts a sequence of arguments to a
  collection-checker-generator and returns a vector containing two
  sequences:

  [<fn arguments> <keyword arguments>]

  fn-arguments are non-keywords meant to pass through unmolested into
  the checker. keyword arguments are optionally parsed by the wrapping
  checker."}
  split-forms
  (partial split-with (complement keyword?)))

;; ## Cascalog-style Checker Helpers

(defn cascalog-checker? [x]
  (-> (meta x)
      (contains? :cascalog-checker)))

(defn fact-line
  "Returns a syntax-quoted list representing the guts of a midje fact
  for the supplied cascalog query and result.

  Note that this fact will check that all tuples inside of `result`
  are generated by the supplied query, in any order. Log Level "
  [result query ll]
  (let [tuple-seq (list `execute query :log-level ll)]
    `[(cond (cascalog-checker? ~result)
            (do ~query => ~result)
            
            (or (fn? ~result) (multifn? ~result))
            (do ~tuple-seq => ~result)
            
            :else (do ~tuple-seq => (just ~result :in-any-order)))]))

(defn build-fact?-
  "Accepts a sequence of fact?- bindings and a midje \"factor\" --
  `fact`, or `future-fact`, for example -- and returns a syntax-quoted
  version of the sequence with all result-query pairs replaced with
  corresponding midje fact-result pairs. For example:

  (build-fact?- '(\"string\" [[1]] (<- [[?a]] ([[1]] ?a))) `fact)
   ;=> (fact <results-of-query> => (just [[1]] :in-any-order)"
  [bindings factor]
  (let [[ll bindings] (pop-log-level bindings)]
    `(~factor
      ~@(loop [[x y & more :as forms] bindings, res []]
          (cond (not x) res
                (or (string? x)
                    (mocking-form? x)) (recur (rest forms) (conj (vec res) x))
                :else (->> (fact-line x y ll)
                           (concat res)
                           (recur more)))))))

(defn build-fact?<-
  "Similar to `build-fact?-`; args must contain a result sequence, a
  query return arg vector, and any number of predicates. The last
  forms can be midje provided or background clauses."
  [args factor]
  (let [[ll :as args] (remove string? args)
        [begin args] (if (keyword? ll)
                       (split-at 2 args)
                       (split-at 1 args))
        [m body] (extract-mockers args)]
    `(~factor ~@begin (<- ~@body) ~@m)))
