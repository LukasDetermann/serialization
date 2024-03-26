package io.determann.serialization.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ConstructorDeSerializationView<TYPE> implements DeSerializationView<TYPE, ConstructorDeSerializationView<TYPE>>
{
   private final List<TypeSetting> settings = new ArrayList<>();
   private final List<Parameter<TYPE, ?>> parameters;
   private final Constructor<TYPE> constructor;

   ConstructorDeSerializationView(List<Parameter<TYPE, ?>> parameters, Constructor<TYPE> constructor)
   {
      this.parameters = parameters;
      this.constructor = constructor;
   }

   private ConstructorDeSerializationView(ConstructorDeSerializationView<TYPE> other)
   {
      this.settings.addAll(other.getSettings());
      this.parameters = other.parameters;
      this.constructor = other.constructor;
   }

   @Override
   public ConstructorDeSerializationView<TYPE> withSetting(TypeSetting setting)
   {
      ConstructorDeSerializationView<TYPE> view = new ConstructorDeSerializationView<>(this);
      view.getSettings().add(setting);
      return view;
   }

   @Override
   public List<TypeSetting> getSettings()
   {
      return settings;
   }

   public List<Parameter<TYPE, ?>> getParameters()
   {
      return parameters;
   }

   public Parameter<TYPE, ?> get(int index)
   {
      List<Parameter<TYPE, ?>> result = parameters.stream().filter(parameter -> parameter.getIndex() == index).collect(Collectors.toList());
      if (result.size() != 1)
      {
         throw new IllegalStateException();
      }
      return result.get(0);
   }

   public Constructor<TYPE> getConstructor()
   {
      return constructor;
   }
}
