package net.zephyr.fnafur.util.Computer;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record ComputerAI(String id, Option<?>... options) {
    public String getId() {
        return this.id;
    }
    public Text getName(){
        return Text.translatable("fnafur.computer.behavior." + id);
    }

    public List<Option<?>> getList() {
        return new ArrayList<>(Arrays.asList(options));
    }
    public Option<?> getOption(String id){
        for(Option<?> op : this.options){
            if(Objects.equals(op.getId(), id)) return op;
        }
        return null;
    }
    public int getOptionIndex(String id){
        for(Option<?> op : this.options){
            if(Objects.equals(op.getId(), id)) return getList().indexOf(op);
        }
        return -1;
    }

    public static class Option<E> {
        E defaultValue;
        String id;
        String dependency;
        boolean invert;

        public Option(String id, E defaultValue) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.dependency = "";
            this.invert = false;
        }


        public Option(String id, E defaultValue, String dependencyId) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.dependency = dependencyId;
            this.invert = false;
        }
        public Option(String id, E defaultValue, String dependencyId, boolean invert) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.dependency = dependencyId;
            this.invert = invert;
        }

        public String getId() {
            return this.id;
        }
        public Text getName(){
            return Text.translatable("fnafur.computer.behavior.option." + id);
        }

        public E getDefaultValue() {
            return this.defaultValue;
        }

        public String getDependency() {
            return this.dependency;
        }

        public boolean isInvert() {
            return invert;
        }

        public Class<?> OptionType(){
            return this.defaultValue.getClass();
        }
    }
}
