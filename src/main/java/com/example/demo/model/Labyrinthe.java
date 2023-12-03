package com.example.demo.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Labyrinthe {
    private int size;//taille du labyrinthe
    private int row;
    private int column;
    private Cell[][] cells;// tableau de deux dimensions qui stock les carres du labyrinthe
    private Cell ongoing;//le carre courant du chemin
    private Stack<Cell> cellsStack ;// pile qui stock le chemin du labyrinthe
    private Cell entry;//entree du labyrinthe
    private Cell exit;//sortie du labyrinthe
    private List<Cell> shortestPath = new ArrayList<>();


    public Labyrinthe(int size) {
        this.size = size;
        this.row = size;
        this.column = size;
        this.cellsStack = new Stack<Cell>();
        this.cells = new Cell[this.row][this.column];
        for(int i=0;i<this.row;i++){
            for(int j=0;j<this.column;j++){
                this.cells[i][j]=new Cell(i,j);
            }
        }

        for(int i=0;i<this.row;i++){
            for(int j=0;j<this.column;j++){
                this.addNeighbors(this.cells[i][j]);
            }
        }

        Random rand = new Random();
        int max =this.size-1;
        int min =0;
        int i=rand.nextInt((max-min)+1)+min;
        this.ongoing = this.cells[0][i];
        this.entry = this.cells[0][i];

        this.ongoing.setWall(3,false);

        int maxS =this.size-1;
        int minS =0;
        int j = rand.nextInt((maxS-minS)+1)+minS;
        this.exit = this.cells[this.size-1][j];
        this.cells[this.size-1][j].setWall(1,false);
        this.ongoing.setChecked(true);

        createPath(this.ongoing);
    }


    //supprimer le mur en commun entre deux carres
    private void removeWall(Cell ongoing,Cell next){
        int x = ongoing.getRow()-next.getRow();
        int y = ongoing.getColumn()-next.getColumn();

        if(x==-1){
            ongoing.setWall(1,false);
            next.setWall(3,false);
        }
        else if(x==1){
            ongoing.setWall(3,false);
            next.setWall(1,false);
        }
        else if(y==-1){
            ongoing.setWall(2,false);
            next.setWall(0,false);
        }
        else if(y==1){
            ongoing.setWall(0,false);
            next.setWall(2,false);
        }
    }


    //methode qui cree le chemin du labyrinthe
    private void createPath(Cell ongoing){
        do{
            if(ongoing.noCheckedNeighbors()){
                Cell next = ongoing.getRandomNeighbor();
                this.cellsStack.add(ongoing);
                this.removeWall(ongoing,next);
                ongoing=next;
                // you have to do something like this for each algorithm of solving in order to draw it
                ongoing.setPath(true);
                shortestPath.add(ongoing);
            }
            else if(!cellsStack.isEmpty()){
                Cell next = this.cellsStack.get(this.cellsStack.size()-1);
                this.cellsStack.remove(next);
                ongoing=next;
            }
        }while(!this.cellsStack.isEmpty());

        for(Cell cell : shortestPath) {
            cell.setPathBfs(true);
        }
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    private void addNeighbors(Cell ongoing){
        int x = ongoing.getRow();
        int y = ongoing.getColumn();
        if(x>0){
            ongoing.neighbors.add(this.cells[x-1][y]);
        }
        if(x<this.row-1){
            ongoing.neighbors.add(this.cells[x+1][y]);
        }
        if(y>0){
            ongoing.neighbors.add(this.cells[x][y-1]);
        }
        if(y<this.column-1){
            ongoing.neighbors.add(this.cells[x][y+1]);
        }
    }

    public Cell getEntry() {
        return entry;
    }

    public Cell getExit() {
        return exit;
    }

//    public void bfs() {
//        PriorityQueue<Cell> priorityQueue = new PriorityQueue<>((c1, c2) -> Integer.compare(c1.getDistance(), c2.getDistance()));
//        entry.setDistance(0);
//        priorityQueue.add(entry);
//
//        while (!priorityQueue.isEmpty()) {
//            Cell current = priorityQueue.poll();
//
//            for (Cell neighbor : current.getNeighbors()) {
//                int tentativeDistance = current.getDistance() + 1; // Assuming each step has a cost of 1
//
//                if (tentativeDistance < neighbor.getDistance()) {
//                    neighbor.setDistance(tentativeDistance);
//                    priorityQueue.add(neighbor);
//                    neighbor.setPathBfs(true);
//                }
//            }
//        }
//    }













}
