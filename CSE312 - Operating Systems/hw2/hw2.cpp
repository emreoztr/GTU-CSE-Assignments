#include <iostream>
using namespace std;


struct page{
    int index[5];
    bool ref;
    int loaded;
    int val[5];
    int page_num;
    bool active;
};

struct page page_table[20];


class PageReplacementAlgorithm{
    public:
        virtual int run(struct page* active_pages[3]){
            return 0;
        }
};

class FIFO: public PageReplacementAlgorithm{
    private:
        int last_ind;

    public:
        FIFO(){
            last_ind = 0;
        }

        virtual int run(struct page** active_pages[3]){
            int oldest_ind = 0;
            int general_oldest_ind = -1;

            for(int i = 0; i < 3; ++i){
                if((*active_pages)[i]->loaded < (*active_pages)[oldest_ind]->loaded){
                    oldest_ind = i;
                }
            }

            while(page_table[last_ind].active){
                last_ind++;
            }
            last_ind++;

            (*active_pages)[oldest_ind]->active = false;
            (*active_pages)[oldest_ind]->loaded = 0;
            (*active_pages)[oldest_ind] = &page_table[last_ind];
            return 0;
        }
};

class SecondChance: public PageReplacementAlgorithm{
    private:
        int last_ind;

    public:
        SecondChance(){
            last_ind = 0;
        }

        virtual int run(struct page** active_pages[3]){
            int oldest_ind = 0;
            int general_oldest_ind = -1;

            for(int i = 0; i < 3; ++i){
                if((*active_pages)[i]->loaded < (*active_pages)[oldest_ind]->loaded && (*active_pages)[i]->ref == false){
                    oldest_ind = i;
                }
            }

            while(page_table[last_ind].active){
                last_ind++;
            }
            last_ind++;

            (*active_pages)[oldest_ind]->active = false;
            (*active_pages)[oldest_ind]->loaded = 0;
            (*active_pages)[oldest_ind] = &page_table[last_ind];
            return 0;
        }
};

class LRU: public PageReplacementAlgorithm{
    public:
        int run(struct page* active_pages[3]){

        }
};

class PageManager{
    private: 
        int counter;
        int num_pages;
        int frame_count;
        
        PageReplacementAlgorithm *algorithm;
        FIFO fifo;
        SecondChance second_chance;
        LRU lru;
    
    public:
    struct page *active_pages[3];
        PageManager(){
            this->num_pages = 3;
            this->frame_count = 5;
            this->counter = 0;

            active_pages[0] = &page_table[0];
            active_pages[1] = &page_table[1];
            active_pages[2] = &page_table[2];

            for(int i = 0; i < 20; ++i){
                page_table[i].active = false;
                page_table[i].loaded = 0;
                page_table[i].ref = false;
                page_table[i].page_num = i;
                for(int j = 0; j < 5; ++j){
                    page_table[i].index[j] = -1;
                }
            }

            active_pages[0]->active = true;
            active_pages[0]->loaded = 1;

            active_pages[1]->active = true;
            active_pages[1]->loaded = 1;

            active_pages[2]->active = true;
            active_pages[2]->loaded = 1;

            algorithm = (PageReplacementAlgorithm*)&fifo;
        }

        void replace_page(){
            int ind = algorithm->run(this->active_pages);
            active_pages[ind]->loaded = counter++;
        }

        void change_algorithm(int algorithm_type){
            if(algorithm_type == 0){
                algorithm = (PageReplacementAlgorithm*)&fifo;
            }
            else if(algorithm_type == 1){
                algorithm = (PageReplacementAlgorithm*)&second_chance;
            }
            else if(algorithm_type == 2){
                algorithm = (PageReplacementAlgorithm*)&lru;
            }
        }

        int find_empty_page(int *page_num){
            while(true){
                for(int i = 0; i < num_pages; i++){
                    for(int j = 0; j < frame_count; ++j){
                        if(active_pages[i]->index[j] == -1){
                            *page_num = i;
                            return j;
                        }
                    }
                }
                replace_page();
            }
            return -1;
        }

        int find_array_index(int index, int *page_num){
            while(true){
                for(int i = 0; i < num_pages; i++){
                    for(int j = 0; j < frame_count; ++j){
                        if(active_pages[i]->index[j] == index){
                            *page_num = i;
                            return j;
                        }
                    }
                }
                replace_page();
            }
            return -1;
        }
        
};

PageManager manager;

class myArrayList{
    private:
        
        int find_index(int arr_index){
            
        }
    public:
    int capacity;
        myArrayList(int capacity){
            int page_num;
            int frame_num;
            this->capacity = capacity;
            for(int i = 0; i < capacity; i++){
                frame_num = manager.find_empty_page(&page_num);
                if(frame_num == -1)
                    break;
                manager.active_pages[page_num]->index[frame_num] = i;
            }
        }

        void set(int index, int value){
            int page_num;
            int frame_num;

            frame_num = manager.find_array_index(index, &page_num);

            manager.active_pages[page_num]->val[frame_num] = value;
        }

        int get(int index){
            int page_num;
            int frame_num;

            frame_num = manager.find_array_index(index, &page_num);

            return manager.active_pages[page_num]->val[frame_num];
        }
};

void bubbleSort(myArrayList arr, int n)
{
    int i, j;
    int swap;
    for (i = 0; i < arr.capacity - 1; i++){
        for (j = 0; j < arr.capacity - i - 1; j++){
            if (arr.get(j) > arr.get(j+1)){
                swap = arr.get(j);
                arr.set(j, arr.get(j+1));
                arr.set(j+1, swap);
            }
        }
    }
}

void swap(int* a, int* b)
{
    int t = *a;
    *a = *b;
    *b = t;
}

int partition (myArrayList *arr, int low, int high)
{
    int pivot = arr->get(high);
    int i = (low - 1);
    int swap;
 
    for (int j = low; j <= high - 1; j++)
    {
        if (arr->get(j) < pivot)
        {
            i++;
            swap = arr->get(j);
            arr->set(j, arr->get(i));
            arr->set(i, swap);
        }
    }
    swap = arr->get(i + 1);
    arr->set(i+1, arr->get(high));
    arr->set(high, swap);
    return (i + 1);
}

void quickSort(myArrayList *arr, int low, int high)
{
    if (low < high)
    {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

void insertionSort(myArrayList &arr)
{
    int i, key, j;
    for (i = 1; i < arr.capacity; i++)
    {
        key = arr.get(i);
        j = i - 1;
 
        while (j >= 0 && arr.get(j) > key)
        {
            arr.set(j + 1, arr.get(j));
            j = j - 1;
        }
        arr.set(j + 1, key);
    }
}

int main()
{
    myArrayList arr(10);
    arr.set(0, 9);
    arr.set(1, 8);
    arr.set(2, 7);
    arr.set(3, 6);
    arr.set(4, 5);
    arr.set(5, 4);
    arr.set(6, 2);
    arr.set(7, 3);
    arr.set(8, 1);
    arr.set(9, 0);

    insertionSort(arr);

    cout << arr.get(0) << endl;
    cout << arr.get(1) << endl;
    cout << arr.get(2) << endl;
    cout << arr.get(3) << endl;
    cout << arr.get(4) << endl;
    cout << arr.get(5) << endl;
    cout << arr.get(6) << endl;
    cout << arr.get(7) << endl;
    cout << arr.get(8) << endl;
    cout << arr.get(9) << endl;

    return 0;
}
