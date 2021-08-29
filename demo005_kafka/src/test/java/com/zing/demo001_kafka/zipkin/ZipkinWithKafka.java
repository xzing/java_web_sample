package com.zing.demo001_kafka.zipkin;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import zipkin2.Span;
import zipkin2.codec.Encoding;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.kafka.KafkaSender;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * create at     2019/9/24 2:00 下午
 *
 * @author zing
 * @version 0.0.1
 */
public class ZipkinWithKafka {

    static String TRACE_ID = "463ac35c9f6413ad";
    static String PARENT_ID = "463ac35c9f6413ab";
    static String SPAN_ID = "48485a3953bb6124";
    static String SAMPLED = "1";


    void addB3Headers(ConsumerRecord<String, byte[]> record) {
        record.headers()
                .add("X-B3-TraceId", TRACE_ID.getBytes(UTF_8))
                .add("X-B3-ParentSpanId", PARENT_ID.getBytes(UTF_8))
                .add("X-B3-SpanId", SPAN_ID.getBytes(UTF_8))
                .add("X-B3-Sampled", SAMPLED.getBytes(UTF_8));
    }

    BlockingQueue<Span> producerSpans = new LinkedBlockingQueue<>();


    //  Set<Map.Entry<String, String>> lastHeaders(Headers headers) {
    //     Map<String, String> result = new LinkedHashMap<>();
    //     headers.forEach(h -> result.put(h.key(), new String(h.value(), Charsets.UTF_8)));
    //     return result.entrySet();
    // }

    final KafkaSender sender = KafkaSender.newBuilder()
            .bootstrapServers("localhost:9092")
            .topic("zipkin")
            .encoding(Encoding.JSON)
            .build();

    @Test
    public void zipkinWithKafkaSender() throws InterruptedException {
        AsyncReporter spanReporter = AsyncReporter.create(sender);

        Tracing tracing = Tracing.newBuilder()
                .localServiceName("my-service")
                .spanReporter(spanReporter)
                .build();
        Tracer tracer = tracing.tracer();

        // Tracing exposes objects you might need, most importantly the tracer
        // Tracer tracer = tracing.tracer();
        brave.Span span1 = tracer.newTrace()
                .name("span1")
                .annotate("init")
                .start();
        span1.tag("key", "value");
        System.out.println(span1.context().spanId());
        Thread.sleep(100);

        TraceContext context = span1.context();
        brave.Span span2 = tracer.nextSpan(TraceContextOrSamplingFlags.newBuilder().context(context).build())
                .name("span2")
                .annotate("step2");
        span2.tag("key2", "value2");
        System.out.println(span2.context().spanId());
        Thread.sleep(100);
        span2.finish();


        brave.Span span3 = tracer.joinSpan(context)
                .name("span3")
                .annotate("step2");
        System.out.println(span3.context().spanId());
        Thread.sleep(100);
        span3.tag("key3", "value3");
        span3.finish();

        brave.Span span4 = tracer.nextSpan();
        span4.name("span4").annotate("step4");
        System.out.println(span4.context().spanId());
        Thread.sleep(100);
        span4.finish();

        brave.Span span5 = tracer.newChild(span4.context());
        span5.name("span5").annotate("step5");
        System.out.println(span5.context().spanId());
        Thread.sleep(100);
        span5.finish();

        span1.finish();

        // Failing to close resources can result in dropped spans! When tracing is no
        // longer needed, close the components you made in reverse order. This might be
        // a shutdown hook for some users.
        tracing.close();
        spanReporter.close();
        sender.close();
    }

    @Test
    public void zipkinTestWithKafka() {


        // Properties props = new Properties();
        // props.put("bootstrap.servers", "172.16.211.163:9092");
        // props.put("acks", "all");
        // props.put("retries", 0);
        // props.put("batch.size", 16384);
        // props.put("linger.ms", 1);
        // props.put("buffer.memory", 33554432);
        // props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Producer<String, String> producer = null;
        // Tracing tracing = null;
        // KafkaTracing kafkaTracing = null;
        // try {
        //     producer = new KafkaProducer<>(props);
        //
        //     tracing = Tracing.newBuilder()
        //             .
        //             .spanReporter(producerSpans::add)
        //             .propagationFactory(
        //                     ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "my-server-name"))
        //             .build();
        //
        //     kafkaTracing = KafkaTracing.newBuilder(tracing).build();
        //     Producer<String, String> tracingProducer = kafkaTracing.producer(producer);
        //
        //     tracingProducer.send(new ProducerRecord<>("demo001_zipkin_kafka", "key", "value"));
        //
        //     // kafkaTracing.
        //
        //
        // } catch (Exception e) {
        //     e.printStackTrace();
        // } finally {
        //     producer.flush();
        //     producer.close();
        //     tracing.close();
        //
        // }


    }
}
