package io.determann.serialization.view;

import java.util.function.Function;

public interface Parameters
{
   public static <TYPE, PROPERTY> Parameter<TYPE, PROPERTY> parameter(String name,
                                                                      Class<PROPERTY> type)
   {
      return new ParameterImpl<>(name, type, null, null);
   }

   public static <TYPE, PROPERTY> Parameter<TYPE, PROPERTY> parameter(String name,
                                                                      Class<PROPERTY> type,
                                                                      Function<Object, PROPERTY> converter)
   {
      return new ParameterImpl<>(name, type, converter, null);
   }

   public static <TYPE, PROPERTY> Parameter<TYPE, PROPERTY> parameter(String name,
                                                                      Class<PROPERTY> type,
                                                                      DeSerializationView<PROPERTY, ?> nested)
   {
      return new ParameterImpl<>(name, type, null, nested);
   }
}