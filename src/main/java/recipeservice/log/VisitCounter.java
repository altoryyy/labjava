package recipeservice.log;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VisitCounter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(VisitCounter.class);
    private final Map<String, AtomicLong> visitCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();

        logger.info("Incrementing visit counter for URI: {}", requestUri);

        chain.doFilter(request, response);
    }

    public void incrementVisit(String url) {
        visitCounts.computeIfAbsent(url, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getVisitCount(String url) {
        AtomicLong counter = visitCounts.get(url);
        return counter != null ? counter.get() : 0;
    }
}