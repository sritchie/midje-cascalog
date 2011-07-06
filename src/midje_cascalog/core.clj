(ns midje-cascalog.core
  (:use [cascalog.testing :only (process?-)]))

(defn- reformat
  "deal with the fact that the first item might be a logging level
  keyword. If so, just keep it."
  [[one & more :as bindings]]
  (let [[kwd bindings] (if (keyword? one)
                         [one more]
                         [nil bindings])
        bindings (remove string? bindings)]
    (if kwd
      (cons kwd bindings)
      bindings)))

(defn fact?-
  "TODO: Docs. Talk about keyword support."
  [& bindings]
  (doseq [[spec tuples] (->> (reformat bindings)
                             (apply process?-)
                             (apply map vector))]
    (fact tuples => (just spec :in-any-order))))

(defmacro fact?<-
  "TODO: Docs. Talk about how we support only one, for now."
  [& args]
  (let [[begin body] (if (keyword? (first args))
                             (split-at 2 args)
                             (split-at 1 args))]
    `(fact?- ~@begin (<- ~@body))))
