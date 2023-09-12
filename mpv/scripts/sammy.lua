-- Sam Brauer's mpv stuff
-- invoke with command like:
-- script-message-to sammy safe-playlist-play-index 23

-- like playlist-play-index command enhanced with bounds check
mp.register_script_message("safe-playlist-play-index", function (idx)
    local count = mp.get_property_number("playlist-count")
    if tonumber(idx) < count then
        mp.set_property_number("playlist-pos", idx)
    end
end)
