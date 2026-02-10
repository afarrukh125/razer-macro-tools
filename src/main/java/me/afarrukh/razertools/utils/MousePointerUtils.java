package me.afarrukh.razertools.utils;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.awt.*;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MousePointerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MousePointerUtils.class);

    public static final double xFactor = 1.71;
    public static final double yFactor = 60.75;

    public static void main(String[] args) {
        String values = """
                    (880, 5500) -> (500, 90)
                    (1760, 11000) -> (1020, 181)
                    (2520, 22000) -> (1460, 362)
                    (3520, 22000) -> (2060, 362)
                    (7040, 44000) -> (4120, 725)
                    (10000, 50000) -> (5840, 823)
                    (25000, 22000) -> (14640, 362)
                    """;

        values.lines()
                .map(String::trim)
                .map(line -> line.split("->"))
                .map(coords -> Arrays.stream(coords)
                        .map(point -> point.replace("(", ""))
                        .map(point -> point.replace(")", ""))
                        .map(point -> point.replace(" ", ""))
                        .map(point -> point.split(","))
                        .map(point -> new Point(parseInt(point[0]), parseInt(point[1])))
                        .toList())
                .map(value -> Pair.of(value.get(0), value.get(1)))
                .map(pointPair -> Pair.of(
                        pointPair.getLeft().getX() / pointPair.getRight().getX(),
                        pointPair.getLeft().getY() / pointPair.getRight().getY()))
                .forEach(System.out::println);

        try (var executorService = newSingleThreadExecutor()) {
            executorService.execute(() -> {
                while (true) {
                    var location =
                            new MultipliedPoint(MouseInfo.getPointerInfo().getLocation(), 20d, 1d);
                    LOG.info("Current mouse location is ({}, {})", location.getX(), location.getY());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private record MultipliedPoint(Point point, double xfactor, double yfactor) {
        public double getX() {
            return point.getX() * xfactor;
        }

        public double getY() {
            return point.getY() * yfactor;
        }
    }
}
