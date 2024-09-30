package com.melonstudios.create.annotation.processor;

import com.melonstudios.create.annotation.processor.util.CompilerUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.melonstudios.createapi.annotation.Incomplete",
        "com.melonstudios.createapi.annotation.UntestedCode"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CreateAnnotationProcessor extends AbstractProcessor {
    public static final CompilerUtils cu = new CompilerUtils();
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        // Initialize the processor
        super.init(env);
        BaseProcessor.init(env);
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // Return the set of annotations supported
        return new HashSet<String>(Arrays.asList(new String[]{
                "com.melonstudios.createapi.annotation.Incomplete",
                "com.melonstudios.createapi.annotation.UntestedCode",
                "com.melonstudios.createapi.annotation.ReflectionConstant"
        }));
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        // Return the Java version supported
        return SourceVersion.RELEASE_8;
    }
    private void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
    private void warn(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }
    private void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }
    private Elements elementUtils() {
        return processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                // Process each element
                note( "Processing: " + cu.getFullQualifiedName(element, elementUtils()) + " because of: " + annotation.getQualifiedName());
                note(Arrays.toString(annotation.getEnclosedElements().toArray()));
                BaseProcessor.processAllValidAnnotations(annotation);
            }
        }
        return true; // No further processing of this annotation type
    }

    private void processIncomplete(TypeElement annotation, Element annotated) {
        String msg = annotation.getTypeParameters().toArray().toString();
    }

    private void processUntestedCode(TypeElement annotation, Element annotated) {

    }
}
