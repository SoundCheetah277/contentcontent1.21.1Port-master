package com.eightsidedsquare.contentcontent.common.entity.ai;

import com.eightsidedsquare.contentcontent.common.entity.ai.sensor.FoodItemEntitySensor;
import com.eightsidedsquare.contentcontent.common.entity.ai.sensor.SquirrelAvoidSensor;
import com.eightsidedsquare.contentcontent.common.entity.ai.sensor.TreetopSensor;
import com.eightsidedsquare.contentcontent.common.entity.squirrel.SquirrelBrain;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.ai.brain.sensor.TemptationsSensor;

import java.util.function.Supplier;

public class CSensorType {
   public static final SensorType<TreetopSensor> TREETOP_SENSOR = register("treetop_sensor", TreetopSensor::new);
   public static final SensorType<FoodItemEntitySensor> FOOD_ITEM_ENTITY_SENSOR = register("food_item_entity_sensor", FoodItemEntitySensor::new);
   public static final SensorType<SquirrelAvoidSensor> SQUIRREL_AVOID_SENSOR = register("squirrel_avoid_sensor", SquirrelAvoidSensor::new);
   public static final SensorType<TemptationsSensor> SQUIRREL_TEMPTATIONS = register("squirrel_temptations", () -> new TemptationsSensor(SquirrelBrain.getTemptItems()));

   private static <U extends Sensor<?>> SensorType<U> register(String id, Supplier<U> factory) {
      return Registry.register(
              Registries.SENSOR_TYPE,
              Identifier.of("contentcontent", id),
              new SensorType<>(factory)
      );
   }
}