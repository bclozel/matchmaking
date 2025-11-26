package io.spring.sample.matchmaking.observability;

import io.micrometer.observation.Observation.Scope;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.handler.TracingObservationHandler.TracingContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.server.observation.ServerRequestObservationConvention;
import org.springframework.web.filter.ServerHttpObservationFilter;

/**
 * {@link ServerHttpObservationFilter} that writes the current Trace context information
 * in a {@code "traceresponse"} HTTP response header.
 *
 * @author Brian Clozel
 * @see <a href=
 * "https://w3c.github.io/trace-context/#trace-context-http-response-headers-format">Trace
 * Context W3C spec</a>
 */
public class TraceResponseHeaderObservationFilter extends ServerHttpObservationFilter {

	/**
	 * Create a {@link TraceResponseHeaderObservationFilter} that will write the
	 * {@code "traceresponse"} HTTP response header.
	 * @param observationRegistry the current observation registry
	 */
	public TraceResponseHeaderObservationFilter(ObservationRegistry observationRegistry) {
		super(observationRegistry);
	}

	/**
	 * Create a {@link TraceResponseHeaderObservationFilter} that will write the
	 * {@code "traceresponse"} HTTP response header.
	 * @param observationRegistry the current observation registry
	 * @param observationConvention the custom observation convention to use.
	 */
	public TraceResponseHeaderObservationFilter(ObservationRegistry observationRegistry,
												ServerRequestObservationConvention observationConvention) {
		super(observationRegistry, observationConvention);
	}

	@Override
	protected void onScopeOpened(Scope scope, HttpServletRequest request, HttpServletResponse response) {
		findObservationContext(request).ifPresent((observationContext) -> {
			TracingContext tracingContext = observationContext.get(TracingContext.class);
			if (tracingContext != null) {
				Span currentSpan = tracingContext.getSpan();
				if (currentSpan != null && !currentSpan.isNoop()) {
					response.setHeader("traceresponse", createTraceResponseHeader(currentSpan));
				}
			}
		});
	}

	private static String createTraceResponseHeader(Span currentSpan) {
		StringBuilder traceresponse = new StringBuilder();
		// version
		traceresponse.append("00-");
		// trace-id
		traceresponse.append(currentSpan.context().traceId());
		traceresponse.append("-");
		// child-id
		traceresponse.append(currentSpan.context().spanId());
		traceresponse.append("-");
		// trace-flags
		traceresponse.append("000000");
		if (currentSpan.context().parentId() != null) {
			traceresponse.append("1");
		}
		else {
			traceresponse.append("0");
		}
		if (Boolean.TRUE.equals(currentSpan.context().sampled())) {
			traceresponse.append("1");
		}
		else {
			traceresponse.append("0");
		}
		return traceresponse.toString();
	}

}