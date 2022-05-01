#!/bin/bash
cd "$(dirname "$0")"
cd ..
java -Dsun.java2d.opengl=true -cp examples/jvm/Lucre2D.jar de.sciss.lucre.canvas.P5Examples --example tree --width 480 --height 480 --full-screen --animate --animate-tri --animate-fps 60
