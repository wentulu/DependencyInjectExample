package cn.stu.cusview.ruiz.annimationprocesser;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import cn.stu.cusview.ruiz.annimations.BindView;

@AutoService(Processor.class)
public class ClassProcesser extends AbstractProcessor {

    ClassName mClassName = ClassName.get("android.app","Activity");
    ClassName fieldName = ClassName.get("java.lang.reflect","Field");
    ClassName exceptionName = ClassName.get("java.lang","Exception");
    ClassName IdMNAme = ClassName.get("cn.stu.cusview.ruiz.inject","R");
    private ProcessingEnvironment mProcessingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessingEnvironment = processingEnvironment;
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Messager messager= mProcessingEnvironment.getMessager();
        Set<? extends Element> set1 =  roundEnvironment.getElementsAnnotatedWith(BindView.class);
        messager.printMessage(Diagnostic.Kind.NOTE,"Size = "+set1.size());

        if (set1.size()>0) {
            ParameterSpec parameterSpec = ParameterSpec.builder(mClassName,"activity").build();
            MethodSpec.Builder builder =  MethodSpec.methodBuilder("findViewByIdAnnotation")
                    .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                    .addParameter(parameterSpec)
                    .beginControlFlow("if(activity!=null)")
                    .addStatement("$T a = activity",mClassName)
                    .endControlFlow();
            for (Element element : set1) {
                if (element.getKind() == ElementKind.FIELD) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "print message :" + element.toString());
                    TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                    messager.printMessage(Diagnostic.Kind.NOTE, "Print Message :" + typeElement.getQualifiedName());
                    builder.addStatement(
                    "try {\n"+
                       " $T field = activity.getClass().getDeclaredField($S);\n"+
                        "field.setAccessible(true);\n"+
                        "field.set(activity,activity.findViewById($T.id."+element.toString()+"));\n"+
                    "} catch ($T e) {\n"+
                       " e.printStackTrace();\n"+
                    "}", fieldName,element.toString(),IdMNAme,exceptionName);
                }
            }

            TypeSpec typeSpec = TypeSpec.classBuilder("MainActivity_FindView")
                    .addMethod(builder.build())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();
            JavaFile javaFile = JavaFile.builder("com.stu.cusview.ruiz", typeSpec)
                    .addFileComment("生成代码，请勿修改")
                    .build();
            try {
                javaFile.writeTo(new File("app/src/main/java"));
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.NOTE, "生成文件失败");
            }
            messager.printMessage(Diagnostic.Kind.NOTE, "生成文件");
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(BindView.class.getCanonicalName());
        return set;
    }
}
