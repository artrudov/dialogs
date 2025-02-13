local function saveMessage(author, text)
    local savedMessage = cjson.encode({author = author, text = text})
    redis.call("LPUSH", KEYS[1], savedMessage)

    return "Message sent"
end

return saveMessage(ARGV[1], ARGV[2])
