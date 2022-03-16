# LyApi

The Official LyMarket Api that we use in every of our projects. We published it as an open source project in case some
customers ask for the src of the plugin we develop for them, they could access the source code of the api.

### Info

this is not a plugin, this get compiled inside your project.

## Dependency:

```xml
<repository>
    <id>lymarket</id>
    <url>https://repo.lydark.org/repository/lymarket/</url>
</repository>
```

```xml
<dependency>
    <groupId>net.lymarket.lyapi</groupId>
    <artifactId>lyapi-spigot</artifactId>
    <version>VERSION</version>
    <scope>compile</scope>
</dependency>
```

## Usage:

```java

public final class YourSexyPluginMainClass extends JavaPlugin {
    
    private final SMain api;
    
    @Override
    public void onEnable() {
        this.api = new SMain(this, "YourSexyPluginName");
        //....
    }
    
    public SMain getApi( ){
        return api;
    }
}

```

## License:

```




