package com.melonstudios.create.annotation.processor.impl;

import com.melonstudios.create.annotation.processor.BaseProcessor;
import com.melonstudios.createapi.annotation.UntestedCode;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class UntestedCodeProcessor extends BaseProcessor<UntestedCode> {
    public UntestedCodeProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public boolean process(UntestedCode annotation, Element element) {
        note("Was called! (UntestedCode)");
        return false;
    }
}
