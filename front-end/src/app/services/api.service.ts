import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Book,
  BooksAll,
  CategoriesBooks,
  SearchBooks,
} from '../models/book.model';
import { LoginResponse, Users } from '../models/user.model';
import { Categories } from '../models/category.model';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  register(user: { username: string; password: string }) {
    return this.http.post(`${this.baseUrl}/api/auth/register`, user);
  }

  login(user: {
    username: string;
    password: string;
  }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      `${this.baseUrl}/api/auth/login`,
      user
    );
  }

  allBooks(): Observable<BooksAll> {
    return this.http.get<BooksAll>(`${this.baseUrl}/api/book/`);
  }

  getBook(id: number): Observable<{ Book: Book }> {
    return this.http.get<{ Book: Book }>(`${this.baseUrl}/api/book/${id}`);
  }

  categoryBooks(category: number): Observable<CategoriesBooks> {
    const params = new HttpParams().set('category', category);
    return this.http.get<CategoriesBooks>(`${this.baseUrl}/api/book/category`, {
      params,
    });
  }

  searchBook(query: string): Observable<SearchBooks> {
    const params = new HttpParams().set('query', query);
    return this.http.get<SearchBooks>(`${this.baseUrl}/api/book/search`, {
      params,
    });
  }

  getUserData(token: String): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/user/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  addComment(
    bookId: number,
    commentData: { content: string; rating: number },
    token: string
  ) {
    return this.http.post(
      `${this.baseUrl}/api/book/${bookId}/comment`,
      commentData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  }

  /* ADMIN Functions */
  getAllUsers(token: String): Observable<Users> {
    return this.http.get<Users>(`${this.baseUrl}/api/admin/users`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  editUserRole(token: String, userId: number, newRole: String) {
    return this.http.put(
      `${this.baseUrl}/api/admin/users/${userId}/role`,
      { newRole },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  }

  createBook(
    token: String,
    title: String,
    author: String,
    plot: String,
    coverImage: String,
    releaseYear: Number,
    categoryID: Number
  ) {
    return this.http.post(
      `${this.baseUrl}/api/admin/book/create`,
      {
        title,
        author,
        plot,
        coverImage,
        releaseYear,
        categoryID,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  }

  deleteBook(token: String, bookID: Number) {
    return this.http.delete(`${this.baseUrl}/api/admin/book/${bookID}/delete`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  getCategories(token: String): Observable<Categories> {
    return this.http.get<Categories>(`${this.baseUrl}/api/admin/categories`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}
