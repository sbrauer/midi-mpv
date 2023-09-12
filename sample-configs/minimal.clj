;; Really minimal config file example.
;; Pause/unpause in response to any midi note-on event.

(def midi-device-name "IAC Driver IAC Bus 1")

(defn midi-event->action
  [{:keys [command] :as event}]
  (when (= :note-on command)
    {:socket-path "/tmp/mpv-socket.0"
     :command     "cycle pause"}))

;; This is it - the config map in all its glory...
{:midi-device-name   midi-device-name
 :midi-event->action midi-event->action}
