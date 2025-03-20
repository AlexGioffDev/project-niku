export interface BooksAll {
  Books: Book[];
}

export interface Book {
  id: number;
  title: string;
  author: string;
  releaseYear: number;
  plot: string;
  coverImage: string;
  categoryId: number;
  categoryName: string;
  comments: Comment[];
}

export interface Comment {
  username: string;
  content: string;
  rating: number;
}

export interface CategoriesBooks {
  category: Category;
}

export interface Category {
  categoryId: number;
  categoryName: string;
  totalBooks: number;
  books: Book[];
}

export interface SearchBooks {
  'Books Found': Book[];
}
