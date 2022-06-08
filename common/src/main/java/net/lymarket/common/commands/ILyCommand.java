/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2022, LyMarket
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * https://github.com/Lydark-Studio/LyApi/blob/master/LICENSE
 *
 * Contact: contact@lymarket.net
 */

package net.lymarket.common.commands;

import net.lymarket.common.commands.response.CommandResponse;

import java.util.ArrayList;

public interface ILyCommand {
    
    CommandResponse command(CommandContext context);
    
    ArrayList < String > tabComplete(TabContext tabContext);
    
}
