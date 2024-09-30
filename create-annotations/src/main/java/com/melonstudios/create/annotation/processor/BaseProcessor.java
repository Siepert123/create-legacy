package com.melonstudios.create.annotation.processor;

import com.melonstudios.create.annotation.processor.util.CompilerUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Base processor class
 *
 * @author moddingforreal
 * @since 0.1.0
 * */
public abstract class BaseProcessor extends AbstractProcessor {
    public final CompilerUtils cu = new CompilerUtils();
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        // Initialize the processor
        super.init(env);
        this.processingEnv = env;
    }
    protected ProcessingEnvironment processingEnv;
    protected void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
    protected void warn(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }
    protected void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }
    protected Elements elementUtils() {
        return processingEnv.getElementUtils();
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        // Return the Java version supported
        return SourceVersion.RELEASE_8;
    }
    @Override
    public abstract Set<String> getSupportedAnnotationTypes();
}
