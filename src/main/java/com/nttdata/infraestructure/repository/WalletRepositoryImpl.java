package com.nttdata.infraestructure.repository;

import com.nttdata.domain.contract.WalletRepository;
import com.nttdata.domain.models.WalletDto;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@ApplicationScoped
public class WalletRepositoryImpl implements WalletRepository {
  private final ReactiveMongoClient reactiveMongoClient;

  public WalletRepositoryImpl(ReactiveMongoClient reactiveMongoClient) {
    this.reactiveMongoClient = reactiveMongoClient;
  }

  @Override
  public Multi<WalletDto> list() {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");
    return collection.find().map(doc->{
      WalletDto walletDto = new WalletDto();
      walletDto.setName(doc.getString("name"));
      walletDto.setLastName(doc.getString("lastName"));
      walletDto.setEmail(doc.getString("email"));
      walletDto.setNroDocument(doc.getLong("nroDocument"));
      walletDto.setTypeDocument(doc.getInteger("typeDocument"));
      walletDto.setNumberCard(doc.getString("numberCard"));
      walletDto.setPassword(doc.getInteger("password"));
      walletDto.setNumberTelephone(doc.getString("numberTelephone"));
      walletDto.setAmount(doc.getDouble("amount"));
      walletDto.setRegistrationDate(doc.getString("registrationDate"));
      walletDto.setCreated_datetime(doc.getString("created_datetime"));
      walletDto.setUpdated_datetime(doc.getString("updated_datetime"));
      walletDto.setActive(doc.getString("active"));
      return walletDto;
    }).filter(auth->{
      return auth.getActive().equals("S");
    });
  }

  @Override
  public Uni<WalletDto> findByNroWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");
    return collection
        .find(new Document("numberTelephone", walletDto.getNumberTelephone())).map(doc->{
          WalletDto wallet = new WalletDto();
          wallet.setName(doc.getString("name"));
          wallet.setLastName(doc.getString("lastName"));
          wallet.setEmail(doc.getString("email"));
          wallet.setNroDocument(doc.getLong("nroDocument"));
          wallet.setTypeDocument(doc.getInteger("typeDocument"));
          wallet.setNumberCard(doc.getString("numberCard"));
          wallet.setPassword(doc.getInteger("password"));
          wallet.setNumberTelephone(doc.getString("numberTelephone"));
          wallet.setAmount(doc.getDouble("amount"));
          wallet.setRegistrationDate(doc.getString("registrationDate"));
          wallet.setCreated_datetime(doc.getString("created_datetime"));
          wallet.setUpdated_datetime(doc.getString("updated_datetime"));
          wallet.setActive(doc.getString("active"));
          return wallet;
        }).filter(s->s.getActive().equals("S")).toUni();
  }

  @Override
  public Uni<WalletDto> addWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");
    Document document = new Document()
        .append("name", walletDto.getName())
        .append("lastName", walletDto.getLastName())
        .append("email", walletDto.getEmail())
        .append("nroDocument", walletDto.getNroDocument())
        .append("typeDocument", walletDto.getTypeDocument())
        .append("numberCard", walletDto.getNumberCard())
        .append("password", walletDto.getPassword())
        .append("numberTelephone", walletDto.getNumberTelephone())
        .append("amount", 0.0)
        .append("registrationDate", this.getDateNow())
        .append("created_datetime", this.getDateNow())
        .append("updated_datetime", this.getDateNow())
        .append("active", "S");
    return collection.insertOne(document).replaceWith(walletDto);
  }

  @Override
  public Uni<WalletDto> updateWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");

    Bson filter = eq("numberTelephone", walletDto.getNumberTelephone());

    Bson updates = combine(
        set("name", walletDto.getName()),
        set("lastName", walletDto.getLastName()),
        set("email", walletDto.getEmail()),
        set("typeDocument", walletDto.getTypeDocument()),
        set("nroDocument", walletDto.getNroDocument()),
        set("password", walletDto.getPassword()),
        set("updated_datetime", this.getDateNow()));
    return collection.updateOne(filter,updates).replaceWith(walletDto);
  }

  @Override
  public Uni<WalletDto> deleteWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");

    Bson filter = eq("numberTelephone", walletDto.getNumberTelephone());
    Bson updates = combine(
        set("updated_datetime", walletDto.getUpdated_datetime()),
        set("active", "N")
    );

    return collection.updateOne(filter,updates).replaceWith(walletDto);
  }

  @Override
  public Uni<WalletDto> depositWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");

    Bson filter = eq("numberTelephone", walletDto.getNumberTelephone());

    return collection
        .find(filter).map(doc->{
          WalletDto wallet = new WalletDto();
          wallet.setName(doc.getString("name"));
          wallet.setLastName(doc.getString("lastName"));
          wallet.setNroDocument(doc.getLong("nroDocument"));
          wallet.setTypeDocument(doc.getInteger("typeDocument"));
          wallet.setNumberCard(doc.getString("numberCard"));
          wallet.setPassword(doc.getInteger("password"));
          wallet.setNumberTelephone(doc.getString("numberTelephone"));
          wallet.setAmount(doc.getDouble("amount"));
          wallet.setRegistrationDate(doc.getString("registrationDate"));
          wallet.setCreated_datetime(doc.getString("created_datetime"));
          wallet.setUpdated_datetime(doc.getString("updated_datetime"));
          wallet.setActive(doc.getString("active"));
          return wallet;
        }).filter(s->s.getActive().equals("S")).toUni().call(doc->{

          Bson updates = combine(
              set("amount", walletDto.getAmount() + doc.getAmount()),
              set("typeOperation", 1)
          )
              ;
          doc.setAmount(walletDto.getAmount() + doc.getAmount());
          return collection.updateOne(filter,updates).replaceWith(doc);
        });

  }

  @Override
  public Uni<WalletDto> withdrawalWallet(WalletDto walletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("wallets");
    ReactiveMongoCollection<Document> collection = database.getCollection("wallet");

    Bson filter = eq("numberTelephone", walletDto.getNumberTelephone());

    return collection
        .find(filter).map(doc->{
          WalletDto wallet = new WalletDto();
          wallet.setName(doc.getString("name"));
          wallet.setLastName(doc.getString("lastName"));
          wallet.setNroDocument(doc.getLong("nroDocument"));
          wallet.setTypeDocument(doc.getInteger("typeDocument"));
          wallet.setNumberCard(doc.getString("numberCard"));
          wallet.setPassword(doc.getInteger("password"));
          wallet.setNumberTelephone(doc.getString("numberTelephone"));
          wallet.setAmount(doc.getDouble("amount"));
          wallet.setRegistrationDate(doc.getString("registrationDate"));
          wallet.setCreated_datetime(doc.getString("created_datetime"));
          wallet.setUpdated_datetime(doc.getString("updated_datetime"));
          wallet.setActive(doc.getString("active"));
          return wallet;
        }).filter(s->s.getActive().equals("S")).toUni().call(doc->{

          Bson updates = combine(set("amount", doc.getAmount() - walletDto.getAmount() ),
              set("typeOperation", 2));
          doc.setAmount(doc.getAmount() - walletDto.getAmount());
          return collection.updateOne(filter,updates).replaceWith(doc);
        });
  }


  private static String getDateNow(){
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date).toString();
  }
}
