package com.melonstudios.create.annotation.processor;

import com.melonstudios.create.annotation.processor.impl.IncompleteProcessor;
import com.melonstudios.create.annotation.processor.impl.UntestedCodeProcessor;
import com.melonstudios.createapi.annotation.Incomplete;
import com.melonstudios.createapi.annotation.UntestedCode;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Base processor class
 *
 * @author moddingforreal
 * @since 0.1.0
 * */
public abstract class BaseProcessor<A extends Annotation> {
    public BaseProcessor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    protected ProcessingEnvironment processingEnv;

    /**
     * All annotation processors
     * */
    public enum Processors {
        INCOMPLETE(IncompleteProcessor.class, Incomplete.class),
        UNTESTED_CODE(UntestedCodeProcessor.class, UntestedCode.class);


        public final Class<? extends BaseProcessor<? extends Annotation>> processor;
        public final Class<? extends Annotation> annotation;
        Processors(Class<? extends BaseProcessor<? extends Annotation>> processor, Class<? extends Annotation> annotation) {
            this.processor = processor;
            this.annotation = annotation;
        }
    }

    public static int init(ProcessingEnvironment processingEnv) {
        // INSTANCES.add(new this.processor.getConstructor(Class));
        int i = 0;
        try {
            for (Processors proccy : Processors.values()) {
                BaseProcessor.INSTANCES.add(proccy.processor.getConstructor(ProcessingEnvironment.class).newInstance(processingEnv));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static Class<? extends BaseProcessor<? extends Annotation>> getProcessor(Class<? extends Annotation> annot) {
        for (Processors proc : Processors.values()) {
            if (proc.annotation == annot)
                return proc.processor;
        }
        return null;
    }

    public static Class<? extends Annotation> getAnnotation(Class<? extends BaseProcessor> proc) {
        for (Processors currproc : Processors.values()) {
            if (currproc.processor.equals(proc)) {
                return currproc.annotation;
            }
        }
        return null;
    }

    public static List<Boolean> processAllValidAnnotations(TypeElement element) {
        List<Boolean> ret = new ArrayList<>();
        for (Processors proc : Processors.values()) {
            //Annotation annot = element.getAnnotation(proc.annotation);
            if (element.getAnnotation(proc.annotation) != null) {
                ret.add(processAnnotation(element.getAnnotation(proc.annotation), element));
            }
        }
        return ret;
    }

    public abstract boolean process(A annotation, Element element);

    public static final List<BaseProcessor<? extends Annotation>> INSTANCES = new ArrayList<>();
    public static BaseProcessor<? extends Annotation> getProcessorInstance(Class<? extends BaseProcessor> clazz) {
        for (BaseProcessor<? extends Annotation> proc : INSTANCES) {
            if (proc.getClass().equals(clazz))
                return proc;
        }
        return null;
    }

    public static <T extends Annotation> boolean processAnnotation(T annotation, Element element) {
        Class<? extends BaseProcessor<? extends Annotation>> clazz = getProcessor(annotation.getClass());
        if (clazz == null)
            return false;
        BaseProcessor<T> proc = (BaseProcessor<T>) getProcessorInstance(clazz);
        if (proc == null)
            return false;
        return proc.process(annotation, element);
    }

    protected void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
    protected void warn(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }
    protected void note(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
