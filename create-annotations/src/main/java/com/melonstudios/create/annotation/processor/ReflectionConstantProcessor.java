package com.melonstudios.create.annotation.processor;

import com.melonstudios.createapi.annotation.ReflectionConstant;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SupportedAnnotationTypes("com.melonstudios.createapi.annotation.ReflectionConstant")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ReflectionConstantProcessor extends BaseProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                // Process each element
                note( "Processing: " + cu.getFullQualifiedName(element, elementUtils()) + " because of: " + annotation.getQualifiedName());
                note(Arrays.toString(annotation.getEnclosedElements().toArray()));
                ReflectionConstant annot = element.getAnnotation(ReflectionConstant.class);
                if (!cu.classExists(processingEnv, annot.value())) {
                    error("Class " + annot.value() + " doesn't exist!");
                }
            }
        }
        return true; // No further processing of this annotation type
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>(Arrays.asList(new String[]{
                "com.melonstudios.createapi.annotation.ReflectionConstant",
        }));
    }
}
