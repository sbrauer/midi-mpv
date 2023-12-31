;; Rather minimal config file example.
;; Pause/unpause in response to any midi note-on event.

(fn [{:keys [command] :as event}]
  (when (= :note-on command)
    {:command     "cycle pause"
     :socket-path "/tmp/mpv-socket.0"}))
