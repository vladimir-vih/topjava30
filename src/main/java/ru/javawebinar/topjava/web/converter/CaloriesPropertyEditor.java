package ru.javawebinar.topjava.web.converter;


import java.beans.PropertyEditorSupport;

public class CaloriesPropertyEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Integer calories = (Integer) getValue();
        return calories == null ? "" : calories.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            setValue(0);
            return;
        }
        try {
            setValue(Integer.parseInt(text));
        } catch (NumberFormatException exception) {
            throw new RuntimeException("Wrong calories");
        }
    }
}
