import java.util.ArrayList;
import java.util.Objects;

public class TreeMap {
    private Node root;
    private int size;

   /**
    *Método que retorna o tamanho da estrutura
    *
    *@return retorna a quantidade de chaves presentes no TreeMap
    */
    public int size() {
    	return this.size;
    }
    
    /**
     * Método que verifica se a estruitura esta vazia ou não
     * 
     * @return retorna true se o objeto TreeMap esta vazio e false se ele estiver ocupado
     */
    public boolean isEmpty() {
    	return this.root == null;
    }

    
    /**
     * Metodo responsável por adicionar elementos no TreeMap a partir de uma chave e um valor 
     * 
     * @param key é a String que sera utilizada para reconhecer unicamente o node;
     * @param value é o valor inteiro ligado a determinada chave
     */
    public void put(String key, int value) {
      
      if (isEmpty()) {
        Node newNode = new Node(key, value);
        this.root = newNode;
        
        this.size++;
        balance(root);
      } else {
        put(this.root, key, value);
      }
    }
	
	private void put(Node current, String key, int value) {
		
		if (current.getKey().compareTo(key) == 0) {
			replace(current, value);
		
		} else if (current.getKey().compareTo(key) > 0) {
			if (current.left == null) {
				Node newNode = new Node(key, value);
				
				current.left = newNode;
				newNode.parent = current;
				
				this.size++;
				
				balance(newNode);
				
				return;
			}
			
			put(current.left, key, value);
		
		} else {
			if (current.right == null) {
				Node newNode = new Node(key, value);
				
				current.right = newNode;
				newNode.parent = current;
				
				this.size++;
				
				balance(newNode);
				
				return;
			}
			
			put(current.right, key, value);
		}
	}
	
	/**
	 * Método que altera o valor atrelado a certa chave
	 * 
	 * @param key a chave do node que se deseja alterar o valor
	 * @param newValue o novo valor que sera atribuido ao node
	 */
	public void replace(String key, int newValue) {
		Node node = search(key);
		
		replace(node, newValue);
	}
	
	private void replace(Node node, int newValue) {
		node.setValue(newValue);
	}
	
	/**
	 * Método responsável por checar o balanceamento durante a inserção do TreeMap seguindo os 5 casos de uma BST rubro-negra
	 * 
	 * @param node o node em que o balanceamento será checado
	 */
	private void balance(Node node) {
		fixupCase1(node);
	}
		/**
		 * Primeiro caso do balanceamento, ele checa se o node em questão é a raiz, em caso positivo ele a torna preta
		 * e em caso negativo segue para o segundp caso
		 * 
		 * @param node o node em que o balanceamento esta sendo checado
		 */
		private void fixupCase1(Node node) {
			if (node.equals(root)) {
				root.turnBlack();
				return;
			} else {
				fixupCase2(node);
			}
			
		}
		
		/**
		 * Segundo caso do balanceamento, ele checa se o pai do node em questão é preto em caso negativo ele segue para o terceiro caso 
		 * e em caso positivo ele encerra a checagem do balanceamento
		 * 
		 * @param node o node em que o balanceamento esta sendo checado
		 */
		private void fixupCase2(Node node) {
			if (node.parent.isBlack()) {
				return;
			} else {
				fixupCase3(node);
			}
		}

		/**
		 * Terceiro caso do balanceamento, ele checa se o tio do node em questão é vermelho,
		 * em caso positivo ambos o pai e o tio do node se tornam pretos e o seu avô se torna vermelho
		 * em seguida o primeiro caso é invocado para o avô do node, caso o contrário convocamos o quarto caso 
		 * 
		 * @param node o node em que o balanceamento esta sendo checado
		 */
		private void fixupCase3(Node node) {
			if (node.getUncle() != null && node.getUncle().isRed()) {
				node.parent.turnBlack();
				node.getUncle().turnBlack();
				node.getGrandParent().turnRed();
				fixupCase1(node.getGrandParent());
				return;
			} else {
				fixupCase4(node);
			}
		}

		/**
		 * Quarto caso do balanceamento, ele checa se o node é um filho a direita e seu pai é um filho a esquerda
		 * em caso positivo ele rotaciona o pai para a esquerda e invoca o quinto caso com a esquerda do node,
		 * se o node for um filho a esquerda e o seu pai for um filho a direita o pai é rotacionado para a direira e 
		 * o quinto caso é invocado com a direita do node, caso o node não se enquadre em nenhum desses casos o quinto passo
		 * é invocado passando o mesmo como parametro 
		 * 
		 * @param node o node em que o balanceamento esta sendo checado
		 */
		private void fixupCase4(Node node) {
			Node next = node;
		
			if (node.isRightChild() && node.parent.isLeftChild()) {
				leftRotation(node.parent);
				next = node.left;
			} else if (node.isLeftChild() && node.parent.isRightChild()) {
				rightRotation(node.parent);
				next = node.right;
			}
		
			fixupCase5(next);
		}

		/**
		 * Quinto caso do balanceamento e o caso final, ele torna o pai do node em preto e seu 
		 * avô em vermelho depois ele checa se o node é filho a esquerda se sim ele rotaciona o node 
		 * para a direita e caso negativo ele rotaciona o node para a esquerda 
		 * 
		 * @param node o node em que o balanceamento esta sendo checado
		 */
		private void fixupCase5(Node node) {
			node.parent.turnBlack();
			node.getGrandParent().turnRed();
		
			if (node.isLeftChild())
				rightRotation(node.getGrandParent());
			else
				leftRotation(node.getGrandParent());
		}
	
	/**
	 * Método que rotaciona determinado node para a esquerda
	 * 
	 * @param node o node que sera rotacionado
	 */
	private void leftRotation(Node node) {
		if (node.equals(root))
			this.root = node.right;
		
		node.right.parent = node.parent;
		
		if (node.parent != null) {
			if (node.isRightChild())
				node.parent.right = node.right;
			else
				node.parent.left = node.right;
		}

		node.parent = node.right;
		
		node.right = node.parent.left;
		
		if (node.parent.left != null)
			node.parent.left.parent = node;
		
		node.parent.left = node;
	}
	
	/**
	 * Método que rotaciona determinado node para a direita
	 * 
	 * @param node o node que sera rotacionado
	 */
	private void rightRotation(Node node) {
		if (node.equals(root))
			this.root = node.left;
		
		node.left.parent = node.parent;
		
		if (node.parent != null) {
			if (node.isRightChild())
				node.parent.right = node.left;
			else
				node.parent.left = node.left;
		}
		
		node.parent = node.left;
		
		node.left = node.parent.right;
		
		if (node.parent.right != null)
			node.parent.right.parent = node;
		
		node.parent.right = node;
	}

	/**
	 * Metodo que remove certa chave do TreeMap
	 * 
	 * @param key chave que deseja ser removida
	 */
	public void remove(String key) {
		Node toRemove = search(key);
		
		if (toRemove == null)
			return;
					
		remove(toRemove);
    }
	
	/**
	 * Metodo reponsavel por retornar que node ira substituir determinado node que esta para ser deletado
	 * 
	 * @param node node que se deseja encontrar o substituto
	 * @return retorna o node que ira substituir o node passado como parametro
	 */
	private Node BSTreplace(Node node) {
		if (node.left != null && node.right != null)
			return predecessor(node.left); 
		
		if (node.isLeaf())
			return null;
		
		if (node.hasOnlyLeftChild())
			return node.left;
		else
			return node.right;
	}
	
	  /**
     * Método que retorna o antecessor de determinado node
     * 
     * @param node node o qual se deseja encontrar o predecessor
     * @return retorna o antecessor do node em questão
     */
	private Node predecessor(Node node) {
		Node aux = node;
		while (aux.right != null)
			aux = aux.right;
		return aux;
	}
	
	/**
	 * Metodo responsável por remover determinado Node da BST rubro-negra
	 * 
	 * @param toRemove node que deseja ser removido
	 */

	private void remove(Node toRemove) {
		Node replace = BSTreplace(toRemove);
		
		boolean bothBlack = (toRemove.isBlack()) && (replace == null || replace.isBlack());
		
		Node parent = toRemove.parent;
		
		if (replace == null) {
			if (toRemove.equals(root))
				this.root = null;
			else {
				if (bothBlack)
					fixDoubleBlack(toRemove);
				else if (toRemove.getSibling() != null)
					toRemove.getSibling().turnRed();
				if (toRemove.isLeftChild())
					parent.left = null;
				else
					parent.right = null;
			}
			this.size--;
			return;
		}
		
		if (toRemove.left == null || toRemove.right == null) {
			if (toRemove.equals(root)) {
				toRemove.setKey(replace.getKey());
				toRemove.setValue(replace.getValue());
				
				toRemove.left = null;
				toRemove.right = null;
			} else {
				if (toRemove.isLeftChild())
					parent.left = replace;
				else
					parent.right = replace;
				
				replace.parent = parent;
				
				if (bothBlack)
					fixDoubleBlack(replace);
				else
					replace.turnBlack();
			}
			this.size--;
			return;
		}
		
		swapValues(replace, toRemove);
		remove(replace);
		
	}
	
	
	/**
	 * Metodo responsável por trocar os valores de dois nodes
	 * 
	 * @param u primeiro node que os valores desejam ser trocados
	 * @param v segundo node que os valores desejam ser trocados
	 */
	private void swapValues(Node u, Node v) {
		String aux = u.getKey();
		int auxValue = u.getValue();
		
		u.setKey(v.getKey());
		u.setValue(v.getValue());
		
		v.setKey(aux);
		v.setValue(auxValue);
	}
	
	/**
	 * Metodo responsavel por checar o balanceamento em uma arvore rubro-negra durante a deleção de um node e 
	 * fazer as alterações necessárias para mante-lo
	 *  
	 * @param node node a partir do qual o balanceamento sera checado 
	 */
	private void fixDoubleBlack(Node node) {
		if (node.equals(root))
			return;
		
		Node sibling = node.getSibling();
		
		if (sibling == null)
			fixDoubleBlack(node.parent);
		else
			removeFixUp(node, sibling);
	}
	
	/**
	 * Continuação do metodo fixDoubleBlack, se trata da parte propriamente responsavel por realizar as adequações 
	 * dada a situação da arvore para a manter balanceada
	 *   
	 * @param node node em que o balanceamento sera checado
	 * @param sibling irmão do node em que o balanceamento esta sendo checado
	 */
	private void removeFixUp(Node node, Node sibling) {
		if (sibling.isRed()) {
			
			sibling.turnBlack();
			node.parent.turnRed();
			
			if (sibling.isLeftChild())
				rightRotation(node.parent);
			else
				leftRotation(node.parent);
			
			fixDoubleBlack(node);
		} else {
			if (sibling.hasRedChild()) {
				
				if (sibling.right != null && sibling.right.isRed()) {
					
					if (sibling.isLeftChild()) {
						
						sibling.right.color = node.parent.getColor();
						
						leftRotation(sibling);
						rightRotation(node.parent);
					} else {
						sibling.right.color = sibling.getColor();
						sibling.color = node.parent.getColor();
						leftRotation(node.parent);
					}
				
				} else {
					if (sibling.isLeftChild()) {
						
						sibling.left.color = sibling.getColor();
						sibling.color = node.parent.getColor();
						
						rightRotation(node.parent);
					} else {
						sibling.left.color = node.parent.getColor();
						
						rightRotation(sibling);
						leftRotation(node.parent);
					}
				}
				node.parent.turnBlack();
			} else {
				sibling.turnRed();
				
				if (node.parent.isBlack())
					fixDoubleBlack(node.parent);
				else
					node.parent.turnBlack();
			}
		}
		
	}
	
	/**
     * Metodo que retorna o valor atrelado a determinada chave
     * 
     * @param key chave referente ao node desejado
     * @return retorna o valor atrelado a chave passada como parametro
     */
	public int get(String key) {
    	Node node = search(key);
    	
    	return node.getValue(); 
    }

    /**
     * Método que retorna um booleano referente a presença de determinada chave no TreeMap
     * 
     * @param key chavce que deseja ser checada
     * @return true caso a chave esteja presente, false em caso contrário
     */
	public boolean containsKey(String key) {
    	return search(key) != null;
    }
    
    /**
     * Método responsável por procurar determinado node na estrutura
     * 
     * @param key chave referente ao node que deseja ser procurado
     * @return retorna o node atrelado a chave passada como parametro 
     */
	private Node search(String key) {
		Node aux = this.root;
		
		while (aux != null) {
			if (aux.getKey() == key)
				return aux;
			
			else if (aux.getKey().compareTo(key) < 0)
				aux = aux.right;
			
			else
				aux = aux.left;
		}
		
		return aux;	
	}

    
    /**
     * Método que percorre toda a BST por profundidade (inOrdem) verificando a presença do valor passado como parametro
     *  
     * @param value valor que se deseja checar a presença na estrutura
     * @return retorna true caso o valor esteja presente em pelo menos um node e falso em caso contrário
     */
	public boolean containsValue(int value) {
    	return containsValue(this.root, value);
    }
    
	private boolean containsValue(Node node, int value) {
    	if (node != null) {
    		
    		if (node.getValue() == value)
    			return true;
    		
    		return containsValue(node.left, value) || containsValue(node.right, value);
    		
    	}
    	
    	return false;
	}
    /**
     * Metodo que retorna a altura da árvore
     * 
     * @return retorna a altura da arvore em forma de inteiro
     */
	public int height() {
    	return height(this.root);
    }
    
    private int height(Node node) {
    	if (node == null)
			return -1;
		else
			return 1 + Math.max(height(node.left), height(node.right));
    }
    
    /**
     * Metodo que retorna a visão da arvore em preOrdem 
     * @return retorna um ArrayList de String composto do toString de todos os nodes em preOrdem
     */
    private ArrayList<String> preOrdem() {
    	ArrayList<String> toString = new ArrayList<String>();
    	preOrdem(this.root, toString);
    	return toString;
    }
    
    private void preOrdem(Node node, ArrayList<String> toString) {
    	if (node != null) {
    		toString.add(node.toString());
    		preOrdem(node.left, toString);
    		preOrdem(node.right, toString);
    	}
    }
    
    @Override
    public String toString() {
    	ArrayList<String> toString = preOrdem();
    	
    	return toString.toString();
    }
}


class Node {
	enum Color {BLACK, RED};
	
	Pair pair;
	Node parent;
  Node left;
  Node right;
  protected Color color;
    

  public Node(String key, int value) {
      this.pair = new Pair(key, value);
      this.color = Color.RED;
  }
    
  public boolean hasRedChild() {
		return this.right.isRed() || this.left.isRed();
	}

	@Override
	public int hashCode() {
		return Objects.hash(pair);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(pair, other.pair);
	}
	
	boolean isLeaf() {
    	return this.left == null && this.right == null;
  }
	
	boolean hasOnlyLeftChild() {
		return right == null && left != null;
	}
	
	boolean hasOnlyRigthChild() {
		return left == null && right != null;
	}

	boolean isLeftChild() {
    	return this.equals(this.parent.left);
  }
	
    
  boolean isRightChild() {
    	return !isLeftChild();
  }
    
  Node getUncle() {
    if (this.parent.isRightChild())
      return this.getGrandParent().left;
    else
      return this.getGrandParent().right;
  }
   	
  Node getSibling() {
    if (this.isLeftChild()) {
      return parent.right;
    } else {
      return parent.left;
    }
  }
  
  Node getGrandParent() {
    return this.parent.parent;
  }

  int getValue() {
    return this.pair.getValue();
  }
  
  void setValue(int newValue) {
    this.pair = new Pair(this.pair.getKey(), newValue);
  }
  
  void setKey(String newKey) {
    this.pair = new Pair(newKey, this.pair.getValue());
  }
  
  String getKey() {
    return this.pair.getKey();
  }

  boolean isRed() {
    return this.color == Color.RED;
  }
  
  boolean isBlack() {
    return this.color == Color.BLACK;
  }
  
  void turnBlack() {
    this.color = Color.BLACK;
  }
  
  void turnRed() {
    this.color = Color.RED;
  }
  
  Color getColor() {
    if (this.color == Color.BLACK) {
      return Color.BLACK;
    } else {
      return Color.RED;
    }
  }
  
  @Override
  public String toString() {
    return "(" + pair.toString() + ")" + "," + this.getColor();
  }
    
}

class Pair {
    private String key;
    private int value;
      
    public Pair(String key, int value) {
      this.key = key;
      this.value = value;
    }
      
    @Override
    public int hashCode() {
      return Objects.hash(key);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Pair other = (Pair) obj;
      return Objects.equals(key, other.key);
    }

    public String getKey() {
      return this.key;
    }
    
    public int getValue() {
      return this.value;
    }
    
    @Override
    public String toString() {
      return key + "," + value;
    }
}
