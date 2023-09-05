(ns midi-mpv.core
  (:gen-class)
  (:require [midi-mpv.app :as app]))

(def usage
  "Usage:
midi-mpv --start config.clj
midi-mpv --list-devices")

(defn -main
  [& args]
  (case (first args)

    "--list-devices"
    (doseq [dev (sort (app/device-descriptions))]
      (println dev))

    "--start"
    (let [script (second args)]
      (assert script "--start requires a script filename.")
      ;; We expect the return value of the script (the final expression) to be a config map.
      (let [config (clojure.main/load-script script)]
        (app/go! config)))

    (println usage)))
