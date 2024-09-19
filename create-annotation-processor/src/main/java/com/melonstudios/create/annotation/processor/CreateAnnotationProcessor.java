package com.melonstudios.create.annotation.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.melonstudios.createapi.annotation.Incomplete",
        "com.melonstudios.createapi.annotation.UntestedCode"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CreateAnnotationProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        // Initialize the processor
        super.init(processingEnv);
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                // Process each element
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing: " + element.getSimpleName());


            }
        }
        return true; // No further processing of this annotation type
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // Return the set of annotations supported
        return new HashSet<String>(Arrays.asList(new String[]{
                "com.melonstudios.createapi.annotation.Incomplete",
                "com.melonstudios.createapi.annotation.UntestedCode"
        }));
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        // Return the Java version supported
        return SourceVersion.RELEASE_8;
    }
}
