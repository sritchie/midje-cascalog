## 1.3.2

* Added custom chatty checker to allow for more dynamic midje fact generation.

- produces (supports :in-order keyword option)
- produces-some (supports :in-order, :no-gaps)
- produces-prefix
- produces-suffix

(Each form can take a log-level keyword as its first argument.)

## 1.3.1

* Moved Cascalog from :dependencies to :dev-dependencies. To use midje-cascalog, you'll have to explicitly include cascalog in your own project. (this prevents version clashes between deps and dev-dependencies.)
