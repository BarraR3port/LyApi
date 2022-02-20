package net.lymarket.common.commands;

import java.util.ArrayList;

public interface ILyCommand {
    
    boolean command( SCommandContext context );
    
    ArrayList < String > tabComplete( STabContext tabContext );
    
}
