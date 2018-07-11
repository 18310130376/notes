

```java
    URL url = ResourceUtils.getURL("classpath:yn-http.properties");
    
    UrlResource resource = new UrlResource(url);

    //其中EncodedResource可以指定具体的编码格式
    EncodedResource enResource = new EncodedResource(resource,"utf-8");

    String result = FileCopyUtils.copyToString(enResource.getReader());

    Properties properties = PropertiesLoaderUtils.loadAllProperties("http.properties")

    File file = ResourceUtils.getFile("classpath:jdbc.properties");

    FileCopyUtils.copy(new File("p0"), new File("p1"));

    BeanUtils.copyProperties(source, target, ignoreProperties);

    Integer num =NumberUtils.parseNumber("12", Integer.class);
    //去掉最后一个字符
    lang = StringUtils.trimTrailingCharacter(lang,',');

    FilenameUtils.getExtension(filename)
```
