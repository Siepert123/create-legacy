package com.melonstudios.create.annotation.processor.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;

public class CompilerUtils {
    /**
     * @return an <code>Element</code>'s fully qualified name
     *
     * @author moddingforreal
     * @since 0.1.0
     * @param elementUtils The {@link javax.annotation.processing.ProcessingEnvironment ProcessingEnvironment}'s elementUtils
     * @param element Element of which to get the fully qualified name
     * */
    public String getFullQualifiedName(Element element, Elements elementUtils) {
        String packageName = getPackageName(element, elementUtils);
        String fullyQualifiedName = getFullyQualifiedNameWithoutPackage(element);

        return packageName + "." + fullyQualifiedName;
    }

    /**
     * @return an <code>Element</code>'s fully qualified name without package
     *
     * @author moddingforreal
     * @since 0.1.0
     * @param element Element of which to get the fully qualified name
     * */
    public String getFullyQualifiedNameWithoutPackage(Element element) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof TypeElement) {
            String className = ((TypeElement) enclosingElement).getQualifiedName().toString();
            if (element instanceof ExecutableElement) {
                return className + "." + element.getSimpleName().toString();  // method or constructor
            } else if (element instanceof VariableElement) {
                return className + "." + element.getSimpleName().toString();  // field
            } else if (element instanceof TypeElement) {
                return ((TypeElement) element).getQualifiedName().toString(); // inner class or interface
            }
        } else if (element instanceof TypeElement) {
            return ((TypeElement) element).getQualifiedName().toString(); // class or interface
        }
        return "";
    }

    /**
     * @return an <code>Element</code>'s package name name
     *
     * @author moddingforreal
     * @since 0.1.0
     * @param elementUtils The {@link javax.annotation.processing.ProcessingEnvironment ProcessingEnvironment}'s elementUtils
     * @param element Element of which to get the package name
     * */
    public String getPackageName(Element element, Elements elementUtils) {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    /**
     * @param className fully qualified class name
     * @return true if the class exists
     * */
    public boolean classExists(ProcessingEnvironment processingEnv, String className) {
        Elements elementUtils = processingEnv.getElementUtils();
        TypeElement typeElement = elementUtils.getTypeElement(className);
        return typeElement != null;
    }

    /**
     * @param className fully qualified class name
     * @return the class if exists, otherwise null
     * */
    public TypeElement getClass(ProcessingEnvironment processingEnv, String className) {
        Elements elementUtils = processingEnv.getElementUtils();
        return elementUtils.getTypeElement(className);
    }

    /**
     * @param methodName method in the class
     * @return true if the method exists
     * */
    public boolean methodExists(TypeElement classElement, String methodName) {
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) enclosedElement;
                if (method.getSimpleName().toString().equals(methodName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param fieldName field in the class
     * @return true if the field exists
     * */
    public boolean fieldExists(TypeElement classElement, String fieldName) {
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.FIELD) {
                VariableElement field = (VariableElement) enclosedElement;
                if (field.getSimpleName().toString().equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
