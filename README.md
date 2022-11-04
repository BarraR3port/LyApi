# LyApi

The Official LyMarket Api that we use in every of our projects. We published it as an open source project in case some
customers ask for the src of the plugin we develop for them, they could access the source code of the api.

### Info

This is not a plugin, this get compiled inside your project.

If you want to use it on minecraft versions higher than 1.16 you need to compile it without the commented tags in the
poms files.

## Dependency:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.BarraR3port</groupId>
    <artifactId>LyApi</artifactId>
    <version>1.2.15</version>
</dependency>
```

## Usage:

```java

public final class YourSexyPluginMainClass extends JavaPlugin {
    
    private static LyApi api;
    
    @Override
    public void onEnable( ){
        api = new LyApi( this , "YourSexyPluginName" );
        /**
         * By default, the message is: "§cYou don't have permission to do that!"
         *
         * If you want to have a custom Error when the player executes a command,
         * and he doesn't have permission, you can do it by doing this:
         **/
        api = new LyApi( this , "YourSexyPluginName" , "§Your Custom No Permission Msg" );
    
    
    }
    
    public static LyApi getApi( ){
        return api;
    }
}

```

## Create and register Commands:

#### Example of a simple command class:

```java
public class ExampleCommand implements ILyCommand {
    
    
    @Command(name = "removehome", permission = "lydark.builder", aliases = {"removehome" , "rmhome" , "removeh" , "rmh"}, usage = "/removehome <home>")
    public boolean command( SCommandContext context ){
        
        if ( !(context.getSender( ) instanceof Player) ) {
            LyTools.getLang( ).sendErrorMsg( context.getSender( ) , "cant-execute-commands-from-console" );
            return true;
        }
        
        if ( context.getArgs( ).length == 0 ) {
            LyTools.getLang( ).sendErrorMsg( context.getSender( ) , "use-this-command" , "command" , "/removehome (nombre)&c." );
            return true;
        }
        Player p = ( Player ) context.getSender( );
        String homeName = context.getArg( 0 ).toLowerCase( );
        Home home = LyTools.getInstance( ).getDatabaseManager( ).getHomes( ).getHome( homeName );
        if ( home.getOwner( ).equalsIgnoreCase( p.getName( ) ) || p.hasPermission( "lytools.admin" ) || p.hasPermission( "lytools.home.removeete" ) ) {
            LyTools.getLang( ).sendMsg( p , (LyTools.getInstance( ).getDatabaseManager( ).getHomes( ).removeHome( context.getArg( 0 ) ) ?
                    "homes.deleted-successfully" :
                    "error.homes.error-deleting") , "home" , homeName );
        }
        return true;
    }
    
    @Tab()
    public ArrayList < String > tabComplete( STabContext sTabContext ){
        ArrayList < String > list = new ArrayList <>( );
        if ( sTabContext.getArgs( ).length == 1 ) {
            list = LyTools.getInstance( ).getDatabaseManager( ).getHomes( ).getPlayerHomes( ( Player ) sTabContext.getSender( ) ).stream( ).map( Home::getName ).collect( Collectors.toCollection( ArrayList::new ) );
        }
        return list;
    }
}
```

### Create a Language :

```java

public class ESLang extends ILang {
    
    public ESLang( ConfigGenerator configGenerator , String prefix , String errorPrefix ){
        super( configGenerator , prefix , errorPrefix );
    }
}

```

### Register commands and Languages:

```java

public final class YourSexyPluginMainClass extends JavaPlugin {
    
    private static LyApi api;
    
    @Override
    public void onEnable( ){
        /**
         * The fastest way to use the api is this:
         * Some stuff will be added and defined by default.
         * */
        api = new LyApi(this, "YourSexyPluginName");
        
        /**
         * You can modify some default stuff like the error message.
         *
         * By default, the message is: "§cYou don't have permission to do that!"
         *
         * If you want to have a custom Error when the player executes a command,
         * and he doesn't have permission, you can do it by doing this:
         **/
        api = new LyApi(this, "YourSexyPluginName", "§Your Custom No Permission Msg");
        
        /**
         * Register your Language:
         *
         * In order to do that, it has to be registered when you initiate and register the API.
         * Example:
         * */
        
        api = new LyApi(this, "YourSexyPluginName", "§Your Custom No Permission Msg", new ESLang(new ConfigGenerator(this, "es.yml"), "&cYourPluginPrefix", "YourPluginError"));
        
        /**
         * Little disclaimer:
         * If you want multiple Language support you can just add the ILang class you decide to use.
         * One example:
         * */
        ILang lang = LangType.valueOf(configManager.getConfig().getString("global.lang")) == LangType.ES ? new ESLang(this, new ConfigGenerator(this, "es.yml"), "&cYourPluginPrefix", "YourPluginError") : new ENLang(this, new ConfigGenerator(this, "en.yml"), "&cYourPluginPrefix", "YourPluginError");
        
        api = new LyApi(this, "YourSexyPluginName", "§Your Custom No Permission Msg", lang);
        
        
        /**
         * Register your commands:
         *
         * In order to do that, it has to be registered after you initiate and register the API.
         * Example:
         * */
        CommandService commandService = new CommandService();
        commandService.registerCommands(new ExampleCommand());
    
    }
    
    public static LyApi getApi( ){
        return api;
    }
}

```

## License:

```license
BSD 3-Clause License

Copyright (c) 2022, BarraR3port
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```




