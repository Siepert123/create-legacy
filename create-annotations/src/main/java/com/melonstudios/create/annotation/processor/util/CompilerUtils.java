package com.melonstudios.create.annotation.processor.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
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
}
