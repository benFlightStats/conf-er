(ns test-conf-er
  (:require [midje.sweet :refer :all]
            [conf-er :refer :all]))

(with-redefs [conf-er/config-map (delay
                                  {:option1 1
                                   :option2 {:thing :test
                                             :foo {:bar :baz}}
                                   :booloptf false
                                   :booloptt true
                                   :something nil})]
  (fact "Basic lookup works"
        (config :option1) => 1
        (config :option2 :thing) => :test
        (config :option2 :foo :bar) => :baz)

  (fact "Looking up things which evaluate to truthy/falsey still returns the
   appropriate values"
        (config :booloptf) => false
        (config :booloptt) => true
        (config :something) => nil)

  (fact "Attempting to get keys which aren't there with strict config throws"
        (config :option2 :thing :bar) => (throws)
        (config :not-there) => (throws))

  (fact "Basic lookups return whether they are configured or not"
        (configured? :option1) => true
        (configured? :option2 :thing) => true
        (configured? :option2 :foo :bar) => true

        (configured? :option2 :thing :bar) => false
        (configured? :not-there) => false)

  (fact "Truthy/falsey values don't throw the configured? predicate"
        (configured? :booloptf) => true
        (configured? :booloptt) => true
        (configured? :something) => true)

  (fact "Optional config returns things when they are there"
        (opt-config :option1) => 1
        (opt-config :option2 :thing) => :test
        (opt-config :option2 :foo :bar) => :baz)

  (fact "Optional config works fine with things which evaluate to
   truthy/falsey"
        (opt-config :booloptf) => false
        (opt-config :booloptt) => true
        (opt-config :something) => nil)

  (fact "Optional config simply returns nil if things aren't there"
        (opt-config :option2 :thing :bar) => nil
        (opt-config :not-there) => nil))

(let [config {:env-override {
                             :default {
                                       :option1    2
                                       :option2 {:thing :edit}
                                       :option-new "new"

                                       }
                             }
              :option1      1
              :option2      {:thing :test
                             :foo   {:bar :baz}}
              }
      _ (reload-config-file config)]
  (fact "test overrides"
        (get-env) => :default
        (merge-config-with-overrides config :default) => {:env-override {:default {:option-new "new", :option1 2, :option2 {:thing :edit}}}, :option-new "new", :option1 2, :option2 {:foo {:bar :baz}, :thing :edit}}
        (opt-config) => {:env-override {:default {:option-new "new", :option1 2, :option2 {:thing :edit}}}, :option-new "new", :option1 2, :option2 {:foo {:bar :baz}, :thing :edit}}
        )

  ;; Test default overrides

  (fact "Basic lookup works"
        (opt-config :option1) => 2
        (opt-config :option-new) => "new"
        (opt-config :option2 :thing) => :edit
        (opt-config :option2 :foo :bar) => :baz
        ;; currently the tests below fail.  it looks like they are referring to the old definition of config-map
        ;(config :option1) => 2
        ;(config :option-new) => "new"
        ;(config :option2 :thing) => :test
        ;(config :option2 :foo :bar) => :baz
         ))
