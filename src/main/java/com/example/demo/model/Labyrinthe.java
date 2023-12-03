package com.example.demo.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Labyrinthe {
    private int size;
    private Cell[][] cells;
    private Cell entry;
    private Cell exit;
    private Random rand = new Random();

    public Labyrinthe(int size) {
        this.size = size;
        initializeCells();
        carveMaze();
        setEntryAndExit();
    }

    private void initializeCells() {
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    private void carveMaze() {
        Stack<Cell> cellStack = new Stack<>();
        Cell current = cells[rand.nextInt(size)][rand.nextInt(size)];
        current.markAsVisited();

        while (hasUnvisitedCells()) {
            ArrayList<Cell> neighbors = getUnvisitedNeighbors(current);

            if (!neighbors.isEmpty()) {
                Cell next = neighbors.get(rand.nextInt(neighbors.size()));
                removeWalls(current, next);
                cellStack.push(current);
                current = next;
                current.markAsVisited();
            } else if (!cellStack.isEmpty()) {
                current = cellStack.pop();
            }
        }
    }

    private ArrayList<Cell> getUnvisitedNeighbors(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getColumn();

        if (row > 0 && !cells[row - 1][col].isVisited()) neighbors.add(cells[row - 1][col]);
        if (row < size - 1 && !cells[row + 1][col].isVisited()) neighbors.add(cells[row + 1][col]);
        if (col > 0 && !cells[row][col - 1].isVisited()) neighbors.add(cells[row][col - 1]);
        if (col < size - 1 && !cells[row][col + 1].isVisited()) neighbors.add(cells[row][col + 1]);

        return neighbors;
    }

    private void removeWalls(Cell current, Cell next) {
        int dx = next.getRow() - current.getRow();
        int dy = next.getColumn() - current.getColumn();

        if (dx == 1) {
            current.removeBottomWall();
            next.removeTopWall();
        } else if (dx == -1) {
            current.removeTopWall();
            next.removeBottomWall();
        } else if (dy == 1) {
            current.removeRightWall();
            next.removeLeftWall();
        } else if (dy == -1) {
            current.removeLeftWall();
            next.removeRightWall();
        }
    }

    private void setEntryAndExit() {
        entry = cells[0][rand.nextInt(size)];
        exit = cells[size - 1][rand.nextInt(size)];
    }

    private boolean hasUnvisitedCells() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isVisited()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < size && column >= 0 && column < size) {
            return cells[row][column];
        }
        return null; // Or throw an exception
    }

    public int getSize() {
        return size;
    }

    public Cell getEntry() {
        return entry;
    }

    public Cell getExit() {
        return exit;
    }
}
