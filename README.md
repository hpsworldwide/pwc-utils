![logo](https://www.hps-worldwide.com/sites/default/files/logo_hps_0.png)

# PowerCARD Utils

### Java 8 Utils 

* [Common Utils](./src/main/java/com/hpsworldwide/powercard/utils)
    * [ByteArrayUtils](./src/main/java/com/hpsworldwide/powercard/utils/ByteArrayUtil.java) : Various useful functions working on byte arrays
    * [CollectionUtils](./src/main/java/com/hpsworldwide/powercard/utils/CollectionUtils.java)
    * [DurationUtils](./src/main/java/com/hpsworldwide/powercard/utils/DurationUtils.java)
    * [HexDumpUtils](./src/main/java/com/hpsworldwide/powercard/utils/HexDumpUtils.java) : Useful operations for dumping hexadecimal data
    * [OperationReferenceManager](./src/main/java/com/hpsworldwide/powercard/utils/OperationReferenceManager.java) : Various useful functions working on byte arrays
    * [ProcessUtils](./src/main/java/com/hpsworldwide/powercard/utils/ProcessUtils.java)
    * [PropertiesUtils](./src/main/java/com/hpsworldwide/powercard/utils/PropertiesUtils.java) : Utils to load properties from different sources
    * [SQLUtils](./src/main/java/com/hpsworldwide/powercard/utils/SQL_Utils.java) : Utils for DB connection purpose, audit connections, data formatting...
    * [StringUtil](./src/main/java/com/hpsworldwide/powercard/utils/StringUtil.java) : String & Byte Array utils
    
* [HTTP Utils](./src/main/java/com/hpsworldwide/powercard/utils/http) : Wrappers around `org.apache.http.impl.client.CloseableHttpClient` that makes some operations even more easy

* [Socket Utils](./src/main/java/com/hpsworldwide/powercard/utils/socket) : Client and server utils to initiate SSL connections, manage connection lifecycle

* [XML Utils](./src/main/java/com/hpsworldwide/powercard/utils/xml)
   * [XML Handling](./src/main/java/com/hpsworldwide/powercard/utils/xml) : Various XML Utils that wrap JAXB to Marshall / Unmarshall XML files, parse XML attributes, format XML data, wrap data validation methods...
   * [XML Injection](./src/main/java/com/hpsworldwide/powercard/utils/xml/inject) : XML parsing functions (using XPath) to inject data in an XML Tree

* [ZIP Utils](./src/main/java/com/hpsworldwide/powercard/utils/zip) : ZIP functions that compress / uncompress data in a stream based on byte arrays
