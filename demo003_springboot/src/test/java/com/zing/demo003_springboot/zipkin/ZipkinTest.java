package com.zing.demo003_springboot.zipkin;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import org.junit.Test;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;


/**
 * create at     2019/9/23 5:10 下午
 *
 * @author zing
 * @version 0.0.1
 */
public class ZipkinTest {


    @Test
    public void zipkinTest() throws InterruptedException {
        // Configure a reporter, which controls how often spans are sent
        //   (the dependency is io.demo001_zipkin_kafka.reporter2:demo001_zipkin_kafka-sender-okhttp3)
        OkHttpSender sender = OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans");
        AsyncReporter spanReporter = AsyncReporter.create(sender);
        // Create a tracing component with the service name you want to see in Zipkin.

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("my-service")
                .spanReporter(spanReporter)
                .build();
        Tracer tracer = tracing.tracer();

        // Tracing exposes objects you might need, most importantly the tracer
        // Tracer tracer = tracing.tracer();
        Span span1 = tracer.newTrace()
                .name("span1")
                .annotate("init")
                .start();
        span1.tag("key", "value");
        System.out.println(span1.context().spanId());
        Thread.sleep(100);

        TraceContext context = span1.context();
        Span span2 = tracer.nextSpan(TraceContextOrSamplingFlags.newBuilder().context(context).build())
                .name("span2")
                .annotate("step2");
        span2.tag("key2", "value2");
        // System.out.println(span2.context().spanId());
        Thread.sleep(100);
        span2.finish();


        Span span3 = tracer.joinSpan(context)
                .name("span3")
                .annotate("step2");
        // System.out.println(span3.context().spanId());
        Thread.sleep(100);
        span3.tag("key3", "value3");
        span3.finish();

        Span span4 = tracer.nextSpan();
        span4.name("span4").annotate("step4");
        // System.out.println(span4.context().spanId());
        Thread.sleep(100);
        span4.finish();

        Span span5 = tracer.newChild(span4.context());
        span5.name("span5").annotate("step5");
        // System.out.println(span5.context().spanId());
        Thread.sleep(100);
        span5.finish();
        span1.context();

        span1.finish();

        System.out.println(span1.context().spanId());


        // Failing to close resources can result in dropped spans! When tracing is no
        // longer needed, close the components you made in reverse order. This might be
        // a shutdown hook for some users.
        tracing.close();
        spanReporter.close();
        sender.close();

    }


    OkHttpSender sender = OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans");
    AsyncReporter spanReporter = AsyncReporter.create(sender);
    long traceId = 0x03db1d960ea13d41l;


    @Test
    public void traceInDifferentProcessStart() throws InterruptedException {
        // Create a tracing component with the service name you want to see in Zipkin.

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("my-service001")
                .spanReporter(spanReporter)
                .build();
        Tracer tracer = tracing.tracer();

        ScopedSpan scopedSpan = tracer.startScopedSpanWithParent("span01", TraceContext.newBuilder()
                .traceId(traceId)
                .parentId(null)
                .spanId(0x01)
                .build());
        scopedSpan.annotate("span01");
        scopedSpan.tag("span01", "test01");
        Thread.sleep(100);
        scopedSpan.finish();
        traceInDifferentProcessStop();
        scopedSpan.finish();
        tracing.close();
    }

    // @Test
    public void traceInDifferentProcessStop() throws InterruptedException {
        // Create a tracing component with the service name you want to see in Zipkin.

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("my-service002")
                .spanReporter(spanReporter)
                .build();
        Tracer tracer = tracing.tracer();

        ScopedSpan scopedSpan = tracer.startScopedSpanWithParent("span02", TraceContext.newBuilder()
                .traceId(traceId)
                .parentId(0x01)
                .spanId(0x02)
                .build());
        scopedSpan.annotate("span02");
        scopedSpan.tag("span02", "test02");
        Thread.sleep(100);
        scopedSpan.finish();
        tracing.close();
        spanReporter.close();
        sender.close();
    }

}
