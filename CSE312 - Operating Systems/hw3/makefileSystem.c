#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdint.h>

#include "FileSystemLib.h"

int create_file_system(uint16_t block_size, char *disk_name);

int main(int argc, char *argv[])
{
    uint16_t block_size;
    char disk_name[64];

    if (argc != 3)
    {
        fprintf(stderr, "Usage: %s <block_size> <inode_count> <filename>\n", argv[0]);
        return 1;
    }
    
    block_size = atoi(argv[1]);
    strcpy(disk_name, argv[2]);

    if(block_size < 1 || block_size > 10000)
    {
        fprintf(stderr, "Block size must be between 1 and 10000\n");
        return 1;
    }
    
printf("%s\n", disk_name);
    if (create_file_system(block_size, disk_name) < 0)
    {
        fprintf(stderr, "Error creating file system\n");
        return 1;
    }

    return 0;
}

int create_file_system(uint16_t block_size, char *disk_name){
    uint8_t* file_system = (uint8_t *)malloc(sizeof(uint8_t) * FILE_SYSTEM_SIZE_BYTES);
    memset(file_system, 0, FILE_SYSTEM_SIZE_BYTES);

    int free_block_size = FILE_SYSTEM_SIZE_BYTES;
    free_block_size -= sizeof(struct SuperBlock);
    free_block_size -= sizeof(struct INode) * INODE_COUNT;
    
    // Create superblock
    SuperBlock *superblock = (SuperBlock *)file_system;
    superblock->block_size = block_size;
    
    superblock->inode_count = INODE_COUNT;
    superblock->block_count = free_block_size / (block_size * KB);
    superblock->free_blocks = superblock->block_count;
    superblock->block_start = FILE_SYSTEM_SIZE_BYTES - (block_size * superblock->block_count * KB);
    superblock->inode_start = superblock->block_start - (sizeof(struct INode) * INODE_COUNT);

    // Create inodes
    INode *inode = (INode *)(file_system + superblock->inode_start);
    inode->active_blocks = 1;
    inode->last_access = (int32_t)time(NULL);
    inode->disk_block[0] = 0;

    // Write to file
    FILE *fp = fopen(disk_name, "wb");
    if (fp == NULL)
    {
        perror("Error opening file");
        return -1;
    }

    if(fwrite(file_system, FILE_SYSTEM_SIZE_BYTES, 1, fp) != 1){
        fprintf(stderr, "Error writing to file\n");
        return -1;
    }

    fclose(fp);
    return 0;
}