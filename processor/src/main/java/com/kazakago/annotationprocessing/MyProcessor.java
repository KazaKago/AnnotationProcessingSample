package com.kazakago.annotationprocessing;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.kazakago.annotationprocessing.*",})
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element classElement : roundEnvironment.getElementsAnnotatedWith(ClassAnnotation.class)) {
            try {
                String packageName = processingEnv.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
                ClassName activityClassName = ClassName.get(packageName, classElement.getSimpleName().toString());
                ClassName generatedClassName = ClassName.get(packageName, classElement.getSimpleName().toString() + "ToastProvider");

                TypeSpec generatedClass = TypeSpec.classBuilder(generatedClassName)
                        .superclass(Types.BaseGeneratedClass)
                        .addField(createActivityField(activityClassName))
                        .addMethod(createConstructor(activityClassName))
                        .addMethods(createShowToastMethods(classElement))
                        .build();
                JavaFile.builder(packageName, generatedClass)
                        .build()
                        .writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private FieldSpec createActivityField(ClassName activityClassName) {
        return FieldSpec.builder(activityClassName, "activity")
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    private MethodSpec createConstructor(ClassName activityClassName) {
        ParameterSpec contextParameter = ParameterSpec.builder(activityClassName, "activity")
                .build();
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextParameter)
                .addStatement("super(activity)")
                .addStatement("this.activity = activity")
                .build();
    }

    private List<MethodSpec> createShowToastMethods(Element classElement) {
        List<MethodSpec> showToastMethods = new ArrayList<>();
        for (Element fieldElement : classElement.getEnclosedElements()) {
            if (fieldElement.getAnnotation(FieldAnnotation.class) != null) {
                MethodSpec showToastMethod = MethodSpec.methodBuilder("show" + StringUtils.capitalize(fieldElement.getSimpleName().toString()) + "Toast")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("showToast(activity.$L)", fieldElement.getSimpleName())
                        .build();
                showToastMethods.add(showToastMethod);
            }
        }
        return showToastMethods;
    }

}
