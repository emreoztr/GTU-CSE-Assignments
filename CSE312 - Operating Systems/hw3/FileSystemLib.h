#include <stdint.h>

#define DISK_BLOCK_COUNT 8
#define MAX_FILE_NAME_SIZE 32
#define FILE_SYSTEM_SIZE_BYTES 16 * 1024 * 1024
#define KB 1024
#define INODE_COUNT 500

typedef struct Directory {
    uint16_t inode;
    char name[MAX_FILE_NAME_SIZE];
}Directory;

typedef struct INode
{
    char name[MAX_FILE_NAME_SIZE];
    uint8_t is_directory;
    uint16_t parent_inode;
    uint32_t size;
    uint8_t active_blocks;
    uint16_t disk_block[DISK_BLOCK_COUNT];
    int32_t last_access;
    uint16_t single_indirect;
    uint16_t double_indirect;
    uint16_t triple_indirect;
} INode;


typedef struct SuperBlock{
    uint16_t block_size;
    uint16_t block_count;
    uint16_t free_blocks;
    uint16_t inode_count;
    uint32_t block_start;
    uint32_t inode_start;
    uint8_t bitmap[2048];
} SuperBlock;