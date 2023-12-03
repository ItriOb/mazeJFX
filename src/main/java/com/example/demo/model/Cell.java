package com.example.demo.model;

import java.util.ArrayList;

public class Cell {
    private int row;
    private int column;
    private boolean visited;
    private boolean[] walls;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.visited = false;
        // Walls are represented in the order: top, right, bottom, left
        this.walls = new boolean[]{true, true, true, true};
    }

    public void markAsVisited() {
        this.visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void removeTopWall() {
        walls[0] = false;
    }

    public void removeRightWall() {
        walls[1] = false;
    }

    public void removeBottomWall() {
        walls[2] = false;
    }

    public void removeLeftWall() {
        walls[3] = false;
    }

    public boolean hasTopWall() {
        return walls[0];
    }

    public boolean hasRightWall() {
        return walls[1];
    }

    public boolean hasBottomWall() {
        return walls[2];
    }

    public boolean hasLeftWall() {
        return walls[3];
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
