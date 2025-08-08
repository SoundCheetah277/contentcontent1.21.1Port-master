package com.eightsidedsquare.contentcontent.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "ContentContent")
public class ContentConfig implements ConfigData {
    @ConfigEntry.Category("client")
    public boolean hideWaterBehindGlass = true;
}
