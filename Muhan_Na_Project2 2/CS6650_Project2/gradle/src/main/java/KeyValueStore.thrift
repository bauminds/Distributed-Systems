service KeyValueStore {
 
    void put(1:i32 key, 2:i32 value),

    string get(1:i32 key),

    bool deleteVal(1:i32 key)
}