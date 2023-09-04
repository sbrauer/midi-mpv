(ns midi-mpv.core
  (:gen-class)
  (:require [midi-mpv.app :as app]
            ;; FIXME
            [midi-mpv.config :as config]))

(defn -main
  [& args]
  ;; FIXME: use a lib to parse args
  (case (first args)

    "--list-devices"
    (doseq [dev (sort (app/device-descriptions))]
      (println dev))

    "--start"
    ;; FIXME: how do we let users pass this in???
    ;; Maybe we get filename as string then... (clojure.main/load-script "config.clj")
    ;; Contract is that we expect the file to (def config ,,,)
    (app/go! config/config)

    ;; help
    (println "Expected --start or --list-devices.")))
