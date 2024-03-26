package io.determann.serialization.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ParameterImpl<TYPE, PROPERTY> implements Parameter<TYPE, PROPERTY>
{
   private final String name;
   private final Class<PROPERTY> type;
   private int index;
   private final Function<Object, PROPERTY> converter;
   private final DeSerializationView<PROPERTY, ?> nestedDeSerializationView;
   private final List<PropertySetting> settings = new ArrayList<>();

   ParameterImpl(String name,
                 Class<PROPERTY> type,
                 Function<Object, PROPERTY> converter,
                 DeSerializationView<PROPERTY, ?> nestedDeSerializationView)
   {
      this.name = requireNonNull(name);
      this.type = requireNonNull(type);
      //noinspection unchecked
      this.converter = converter == null ? o -> ((PROPERTY) o) : converter;
      this.nestedDeSerializationView = nestedDeSerializationView;
   }

   ParameterImpl(int index, Parameter<TYPE, PROPERTY> parameter)
   {
      this(parameter);
      this.index = index;
   }

   private ParameterImpl(Parameter<TYPE, PROPERTY> other)
   {
      ParameterImpl<TYPE, PROPERTY> other1 = (ParameterImpl<TYPE, PROPERTY>) other;

      this.name = other1.name;
      this.type = other1.type;
      this.converter = other1.converter;
      this.nestedDeSerializationView = other1.nestedDeSerializationView;
   }

   @Override
   public Parameter<TYPE, PROPERTY> withSetting(PropertySetting setting)
   {
      requireNonNull(setting);

      Parameter<TYPE, PROPERTY> copy = new ParameterImpl<>(this);
      copy.getSettings().add(setting);
      return copy;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public Class<PROPERTY> getType()
   {
      return type;
   }

   @Override
   public int getIndex()
   {
      return index;
   }

   @Override
   public Function<Object, PROPERTY> getConverter()
   {
      return converter;
   }

   @Override
   public List<PropertySetting> getSettings()
   {
      return settings;
   }

   @Override
   public Optional<DeSerializationView<PROPERTY, ?>> getNestedWrite()
   {
      return Optional.ofNullable(nestedDeSerializationView);
   }
}
