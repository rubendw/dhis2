/**
 * <p>Implements a sortable hashtable which accepts non-<code>null</code> String
 * keys and non-<code>null</code> values.</p>
 *
 * <p>This object is primarily intended to address the shortcomings of using an
 * Object as a hashtable, by managing the names of the properties which are
 * stored in an Object.</p>
 *
 * <p>HashTable does not define a hashing function, as Object's built-in
 * hashing is used for storing and retrieving items.</p>
 *
 * <p>Initial values are taken from any Objects passed into the constructor:
 * if another HashTable is given as an argument, its contents will be taken.</p>
 *
 * @constructor
 */
var HashTable = function()
{
   /**
    * Storage object - values are stored as properties whose names are hashtable
    * keys.
    *
    * @type Object
    * @private
    */
    this._store = {};

   /**
    * A list of hashtable keys.
    *
    * @type Array
    * @private
    */
    this._keys = [];

    for (var i = 0; i < arguments.length; i++)
    {
        this._putFromObject(arguments[i]);
    }
};

/**
 * Puts all properties from a given HashTable or Object into the hashtable.
 *
 * @param {Object} source an object whose values will be put into the hashtable.
 * @private
 */
HashTable.prototype._putFromObject = function(source)
{
    if (source.constructor == HashTable)
    {
        source.forEach(function(k, v)
        {
            this.put(k, v);
        }, this);
    }
    else
    {
        for (property in source)
        {
            if (source.hasOwnProperty(property))
            {
                this.put(property, source[property]);
            }
        }
    }
};

/**
 * Clears the hashtable.
 */
HashTable.prototype.clear = function()
{
    this._store = {};
    this._keys = [];
};

/**
 * An iterator which calls the given function, passing key, value, item index and a
 * reference to this Hashtable for each item in the hashtable.
 *
 * @param {Function} callback the function to be called for each item.
 * @param {Object} context an optional context object for the calls to block.
 *
 * @see "Enumerating Javascript Objects", http://dean.edwards.name/weblog/2006/07/enum/
 */
HashTable.prototype.forEach = function(callback, context)
{
    for (var i = 0, l = this._keys.length; i < l; i++)
    {
        callback.call(context, this._keys[i], this._store[this._keys[i]], i, this);
    }
};

/**
 * Retrieves the item with the given key.
 *
 * @param {String} key the key for the item to be retrieved.
 * @return the item stored in this HashTable with the given key if one exists,
 *         <code>null</code> otherwise.
 */
HashTable.prototype.get = function(key)
{
    var result = null;
    if (typeof(this._store[key]) != "undefined")
    {
        result = this._store[key];
    }
    return result;
};

/**
 * Determines if the hashtable contains the given key.
 *
 * @param {String} key the key to be searched for.
 * @return <code>true</code> if this HashTable contains the given key,
 *         <code>false</code> otherwise.
 * @type Boolean
 */
HashTable.prototype.hasKey = function(key)
{
    var result = false;
    this.forEach(function(k)
    {
        if (key == k)
        {
            result = true;
            return true;
        }
    });
    return result;
};

/**
 * Determines if the hashtable contains the given value.
 *
 * @param {Object} value the value to be searched for.
 * @return <code>true</code> if this HashTable contains the given value,
 *         <code>false</code> otherwise.
 * @type Boolean
 */
HashTable.prototype.hasValue = function(value)
{
    var result = false;
    this.forEach(function(k, v)
    {
        if (value == v)
        {
            result = true;
            return true;
        }
    });
    return result;
};

/**
 * Creates Object representations of the items in the hashtable.
 *
 * @return the items in the hashtable, represented as Objects with "key"
 *         and "value" properties.
 * @type Array
 */
HashTable.prototype.items = function()
{
    var items = [];
    this.forEach(function(k, v)
    {
        items.push({"key": k, "value": v});
    });
    return items;
};

/**
 * Retrieves the hashtable's keys.
 *
 * @return the hashtable's keys.
 * @type Array
 */
HashTable.prototype.keys = function()
{
    var keys = [];
    this.forEach(function(key)
    {
        keys.push(key);
    });
    return keys;
};

/**
 * Puts an item into the hashtable.
 *
 * @param {String} key the key under which the item should be stored.
 * @param {Object} value the item to be stored.
 */
HashTable.prototype.put = function(key, value)
{
    if (key == undefined || key == null || typeof(key) != "string"
        || value == undefined || value == null)
    {
        return;
    }

    if (typeof(this._store[key]) == "undefined")
    {
        this._keys.push(key);
    }

    this._store[key] = value;
};

/**
 * Removes an item from the hashtable and returns it.
 *
 * @param {String} key the key for the item to be removed.
 * @return the item which was removed, or <code>null</code> if the item did not
 *         exist.
 */
HashTable.prototype.remove = function(key)
{
    var result = null;
    for (var i = 0, l = this._keys.length; i < l; i++)
    {
        if (key == this._keys[i])
        {
            result = this._store[key];
            delete(this._store[key]);
            this._keys.splice(i, 1);
            break;
        }
    }
    return result;
};

/**
 * Determines the number of entries in the hashtable.
 *
 * @return the number of entries in this HashTable.
 * @type Number
 */
HashTable.prototype.size = function()
{
    return this._keys.length;
};

/**
 * Sorts the keys of the hashtable.
 *
 * @param {Function} comparator an optional function which will be used to sort
 *                              the keys - if not provided, they will be sorted
 *                              lexographically (in dictionary order).
 */
HashTable.prototype.sort = function(comparator)
{
    if (typeof(comparator) == "function")
    {
        this._keys.sort(comparator);
    }
    else
    {
        this._keys.sort();
    }
};

/**
 * Creates a String representation of the hashtable.
 *
 * @return a String representation of this {@link HashTable}.
 * @type String
 */
HashTable.prototype.toString = function()
{
    var result = "{";
    this.forEach(function(key, value, index)
    {
        if (index != 0)
        {
            result += ", ";
        }
        result += key + ": " + value;
    });
    result += "}";
    return result;
};

/**
 * Updates the hashtable with the values contained in a given {@link HashTable}
 * or Object.
 *
 * @param {Object} source an object whose values will be put into the hashtable.
 */
HashTable.prototype.update = function(source)
{
    this._putFromObject(source);
};

/**
 * Retrieves the hashtable's values.
 *
 * @return the hashtable's values.
 * @type Array
 */
HashTable.prototype.values = function()
{
    var values = [];
    this.forEach(function(key, value)
    {
        values.push(value);
    });
    return values;
};