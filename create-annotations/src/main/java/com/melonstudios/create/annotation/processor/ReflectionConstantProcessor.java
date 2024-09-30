package com.melonstudios.create.annotation.processor;

import com.melonstudios.create.annotation.processor.util.CompilerUtils;
import com.melonstudios.createapi.annotation.ReflectionConstant;

import javax.annotation.processing.*;
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
                if (!annot.suppress() &&
                        !(ReflectionType.exists(ReflectionType.checkType(annot.value()),
                                annot.value(), cu, processingEnv))) {
                    if (annot.altMsg().isEmpty()) {
                        error(ReflectionType.checkType(annot.value()).name + annot.value() + " couldn't be found!");
                    } else {
                        error(ReflectionType.checkType(annot.value()).name + annot.value() + " :\n\t" + annot.altMsg());
                    }
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

    /**
     * Type of thing to be reflected on
     * */
    private enum ReflectionType {
        INDETERMINATE("Indeterminate element"),
        CLASS("Class"),
        METHOD("Method"),
        FIELD("Field");
        public final String name;
        ReflectionType(String name) {
            this.name = name + " ";
        }

        public static ReflectionType checkType(String reflectionString) {
            if (reflectionString.contains("#")) {
                if (reflectionString.contains("()")) {
                    return ReflectionType.METHOD;
                }
                return  ReflectionType.FIELD;
            }
            return ReflectionType.CLASS;
        }

        public static boolean exists(ReflectionType type, String reflectionStr,
                                     CompilerUtils cu, ProcessingEnvironment env) {
            switch (type) {
                default:
                case INDETERMINATE:
                    return false;
                case CLASS:
                    return cu.classExists(env, reflectionStr);
                case METHOD:
                    TypeElement clazz_method = cu.getClass(env, reflectionStr.split("\\#")[0]);
                    return cu.methodExists(clazz_method, reflectionStr.split("\\#")[1].split("\\(\\)")[0]);
                case FIELD:
                    TypeElement clazz_field = cu.getClass(env, reflectionStr.split("\\#")[0]);
                    return cu.fieldExists(clazz_field, reflectionStr.split("\\#")[1]);
            }
        }
    }
}
