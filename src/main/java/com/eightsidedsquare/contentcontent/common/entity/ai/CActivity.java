//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.eightsidedsquare.contentcontent.common.entity.ai;

import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registry;

public class CActivity {
    public static final Activity EAT = register("eat");

    public CActivity() {
    }

    private static Activity register(String id) {
        id = "contentcontent:" + id;
        return (Activity)Registry.register(Registry.field_18796, id, new Activity(id));
    }
}
