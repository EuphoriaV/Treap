#include<iostream>
#include<random>
#include<ctime>
using namespace std;


class Node {
public:
	Node* left;
	Node* right;
	int x;
	double y;
	int w;

	Node() {
		this->left = NULL;
		this->right = NULL;
		this->y = rand();
		this->w = 1;
	}

	Node(Node* left, Node* right, int x) {
		this->left = left;
		this->right = right;
		this->x = x;
		this->y = rand();
		this->w = 1;
	}
};

Node* tree = NULL;

int getW(Node* t) {
	if (t == NULL)
		return 0;
	else
		return t->w;
}

void update(Node* t) {
	if (t != NULL)
		t->w = getW(t->left) + getW(t->right) + 1;
}

pair<Node*, Node*> split(Node* t, int k) {
	if (t == NULL) {
		return { NULL , NULL };
	}
	else if (k > t->x) {
		pair<Node*, Node*> ans = split(t->right, k);
		t->right = ans.first;
		update(t->right);
		return { t, ans.second };
	}
	else {
		pair<Node*, Node*> ans = split(t->left, k);
		t->left = ans.second;
		update(t->left);
		return { ans.first, t };
	}
}

Node* merge(Node* t1, Node* t2) {
	if (t2 == NULL) {
		return t1;
	}
	else if (t1 == NULL) {
		return t2;
	}
	else if (t1->y > t2->y) {
		t1->right = merge(t1->right, t2);
		update(t1->right);
		return t1;
	}
	else {
		t2->left = merge(t1, t2->left);
		update(t2->left);
		return t2;
	}
}

Node* insert(Node* t, int k) {
	pair<Node*, Node*> ans = split(t, k);
	t = merge(merge(ans.first, new Node(NULL, NULL, k)), ans.second);
	update(t);
	return t;
}

Node* remove(Node* t, int k) {
	if (t->x == k) {
		t = merge(t->left, t->right);
	}
	else if (k < t->x) {
		t->left = remove(t->left, k);
	}
	else if (k > t->x) {
		t->right = remove(t->right, k);
	}
	update(t);
	return t;
}

void print(Node* t) {
	if (t != NULL) {
		print(t->left);
		cout << t->x << " ";
		print(t->right);
	}
}

int getK(Node* t, int k) {
	int bebra = getW(t->right);
	if (bebra + 1 == k) {
		return t->x;
	}
	else if (bebra + 1 > k) {
		return getK(t->right, k);
	}
	else {
		return getK(t->left, k - bebra - 1);
	}
}

int main() {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);
	srand(time(NULL));
	int n;
	cin >> n;
	bool first = true;
	for (int i = 0; i < n; i++) {
		int a, b;
		cin >> a >> b;
		if (a == 1) {
			if (first) {
				tree = new Node(NULL, NULL, b);
				first = false;
			}
			else {
				tree = insert(tree, b);
			}
		}
		else if (a == -1) {
			tree = remove(tree, b);
		}
		else {
			cout << getK(tree, b) << endl;
		}
	}
}
