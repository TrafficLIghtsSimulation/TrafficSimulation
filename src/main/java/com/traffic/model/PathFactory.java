package com.traffic.model;

import com.traffic.model.enums.Direction;
import com.traffic.model.enums.MovementType;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.animation.PathTransition;
import javafx.util.Duration;


public class PathFactory {

    public static Path getPath(Direction from, MovementType movementType) {
        if (from == Direction.NORTH) {
            switch (movementType) {
                case MovementType.FORWARD: return pathNorthToSouth();
                case MovementType.RIGHT:   return pathNorthToWest();
                case MovementType.LEFT:    return pathNorthToEast();
            }
        } else if (from == Direction.SOUTH) {
            switch (movementType) {
                case MovementType.FORWARD: return pathSouthToNorth();
                case MovementType.RIGHT:   return pathSouthToEast();
                case MovementType.LEFT:    return pathSouthToWest();
            }
        } else if (from == Direction.EAST) {
            switch (movementType) {
                case MovementType.FORWARD: return pathEastToWest();
                case MovementType.RIGHT:   return pathEastToNorth();
                case MovementType.LEFT:    return pathEastToSouth();
            }
        } else if (from == Direction.WEST) {
            switch (movementType) {
                case MovementType.FORWARD: return pathWestToEast();
                case MovementType.RIGHT:   return pathWestToSouth();
                case MovementType.LEFT:    return pathWestToNorth();
            }
        }
        return new Path(); // fallback boş path
    }
    private static Path pathSouthToEast() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        path.getElements().add(new CubicCurveTo(
                388, 358,     // 1. kontrol noktası
                426, 300,     // 2. kontrol noktası
                426, 238      // bitiş noktası
        ));

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathSouthToNorth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathSouthToWest() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathNorthToEast() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathNorthToWest() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathNorthToSouth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathWestToEast() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathWestToSouth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathWestToNorth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathEastToWest() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathEastToNorth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }
    private static Path pathEastToSouth() {
        Path path = new Path();
        path.getElements().add(new MoveTo(388,550));
        path.getElements().add(new LineTo(388,358));
        ArcTo arc = new ArcTo();
        arc.setX(426);       // hedef X
        arc.setY(238);       // hedef Y
        arc.setRadiusX(70);  // virajın yatay yarıçapı (ayarlanabilir)
        arc.setRadiusY(70);  // virajın dikey yarıçapı (ayarlanabilir)
        arc.setSweepFlag(true);   // saat yönünde (sağa doğru)
        arc.setLargeArcFlag(false); // küçük yay
        path.getElements().add(arc);

        // 4. Düz sola devam
        path.getElements().add(new LineTo(0, 238));

        return path;
    }










}
