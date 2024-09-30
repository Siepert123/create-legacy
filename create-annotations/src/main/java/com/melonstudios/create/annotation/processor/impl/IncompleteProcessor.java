package com.melonstudios.create.annotation.processor.impl;

import com.melonstudios.create.annotation.processor.BaseProcessor;
import com.melonstudios.createapi.annotation.Incomplete;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class IncompleteProcessor extends BaseProcessor<Incomplete> {

    public IncompleteProcessor(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public boolean process(Incomplete annotation, Element element) {
        note("Was called! (Incomplete)");
        return false;
    }
}
