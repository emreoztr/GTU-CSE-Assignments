#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "FileSystemLib.h"

void dumpe2fs(SuperBlock *superblock);

int main(int argc, char *argv[])
{
    if(argc < 3){
        fprintf(stderr, "Wrong command!\n");
        return 1;
    }

    char filename[64];
    char command[64];

    strcpy(filename, argv[1]);
    strcpy(command, argv[2]);

    FILE *fp = fopen(filename, "rb+");
    if(fp == NULL){
        fprintf(stderr, "File not found!\n");
        return 1;
    }

    SuperBlock *superblock = (SuperBlock *)malloc(sizeof(SuperBlock));
    fread(superblock, sizeof(SuperBlock), 1, fp);

    fclose(fp);

    if(strcmp(command, "dumpe2fs") == 0){
        dumpe2fs(superblock);
    }
    

    return 0;
}

void dumpe2fs(SuperBlock *superblock){
    printf("Block size: %d\n", superblock->block_size);
    printf("Block count: %d\n", superblock->block_count);
    printf("Inode count: %d\n", superblock->inode_count);
    printf("Block start: %d\n", superblock->block_start);
    printf("Inode start: %d\n", superblock->inode_start);
    printf("Free blocks: %d\n", superblock->free_blocks);
}